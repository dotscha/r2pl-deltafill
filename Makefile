
OS = $(shell uname)
ifeq ($(OS),Windows_NT)
include mkdefs.win32
else
include mkdefs.linux
endif

JAVA_CP = .
JAVA_SRC = Main.java DPoint.java Slice.java Plotter.java Condition.java SinTab.java Code.java P3D.java Polygon.java Matrix.java
JAVA_CLASSES = *.class

#BITMAP = pic_exhale.prg
#BITMAP = background.prg
BITMAP = Mech.prg Mech_omega.prg

all: disk.d64

run: main_exo.prg
	$(EMU) main_exo.prg

disk.d64 : main_exo.prg
	$(MKD64) -o disk.d64 -m cbmdos -i " 2017" -d "DELTA-FILL" -f main_exo.prg -n START -w

main_exo.prg: main.prg
	$(EXO) sfx 0x3000 -t4 -n -o $@ $^
	$(RM) main.lst

main.prg: main.asm plotters.asm sintab.asm renderer0.asm $(BITMAP)
	$(ASS) main

test: $(LIB)
	$(JAVA) -cp $(LIB) Main -test
	#-test

sintab.asm : $(LIB)
	$(JAVA) -cp $(LIB) Main -sintable > sintab.asm

renderer.asm : $(LIB) Makefile omega2x.txt
	$(JAVA) -cp $(LIB) Main -renderer mx > renderer.asm

SPLIT = 311

renderer0.asm: renderer.asm Makefile
	head -n $(SPLIT) $< > renderer0.asm
	tail -n +$(SPLIT) $< > renderer1.asm

$(LIB) : $(JAVA_SRC)
	$(JAVAC) -cp $(JAVA_CP) $(JAVA_SRC)
	$(JAR) $(LIB) $(JAVA_CLASSES)

clean :
	$(RM) $(JAVA_CLASSES) $(LIB) main*.prg *.d64 sintab.asm renderer.asm main.lst || true

