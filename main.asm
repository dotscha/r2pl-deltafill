	include default.ini

	OUTRADIX 10

music = 0

shift_x = 9
colors = $3800
bitmap = $4000+shift_x*8
source_row0 =  8
source_row1 = 16
;source_block0 = 11
;source_block1 = 21

source = bitmap_copy+shift_x*8-source_row0*320
;source = bitmap_copy+shift_x*8-source_block0*256
;source = $4000+shift_x*8-source_block0*256

sintab = $fd00 - $0300		;$0200
mask   = sintab+$0100
color3 = sintab+$0200
color2 = $0500		;unused
color1 = $0600		;unused
color12 = $0700		;unused
color21 = $0800		;unused
color0 = sintab+1
color_ = sintab

ang = $d0

CODE = $3000

	org CODE-2
	adr CODE

main:
	sei
	sta $ff3f

	; init music if present

	if music
	lda #$4c
	cmp $1000
	lda #0
	jsr $1000
	endif

	if 0
	;copy bitmap to source
	ldy #source_block1-source_block0
	ldx #0
-	lda $ff00 & bitmap+source_block0*256,x
	sta $ff00 & source+source_block0*256,x
	inx
	bne -
	inc - + 2
	inc - + 5
	dey
	bne -
	
	endif
	
	;set bitmap mode and colors

	lda #$00	;#$98
	sta $ff15
	lda #$41	;#$81
	sta $ff16

	lda $ff06
	ora #$20
	sta $ff06
	lda $ff07
	ora #$10
	sta $ff07
	lda $ff12
	and #%11000011
	ora #(bitmap/1024)
	sta $ff12
		
	lda #hi(colors)
	sta $ff14
	
	
	ldx #0
c1:
	txa
	and #7
	tay
	lda mask_bits,y
	sta mask,x
	eor #255
	sta color3,x
	;and #%01010101
	;sta color1,x
	;asl
	;sta color2,x
	;lda color3,x
	;and #%01100110
	;sta color12,x
	;eor color3,x
	;sta color21,x
	inx
	bne c1

	lda #0
	sta ang

c2:
	lda ang
	eor #255
	tay
	iny

	jsr update_sintab

	ldy ang
	lda render_lo,y
	sta + +1
	lda render_hi,y
	sta + +2
+	jsr render

	;waiting for next frame
	inc $ff19
	lda #$cc
	cmp $ff1d
	bne *-3
	dec $ff19

	;call music player if present
	if music
	dec $ff19
	lda #$4c
	cmp $1003
	bne +
	jsr $1003
+	inc $ff19
	endif

	inc ang

	if 1

	jmp c2

	else 

	bne c2
	jmp *

	endif


mask_bits:
	byt [2]%00111111
	byt [2]%11001111
	byt [2]%11110011
	byt [2]%11111100

update_sintab:

sin_offset = 5
	include sintab.asm

	include plotters.asm

color_r = color1
color_g = color2
color_b = color3
color_a = color0

	align 256

render_lo:
i	set 0
	rept 256
	byt lo(render_{"\{i}"})
i	set i+1
	endm
render_hi:
i	set 0
	rept 256
	byt hi(render_{"\{i}"})
i	set i+1
	endm

	align 1024

	binclude "Mech_omega.prg",2
	;binclude "Mech.prg",2

render:
	clc
	include renderer0.asm
	jmp render_jump

	align 256

bitmap_copy:
	binclude "Mech.prg",(2+2048+source_row0*320),320*(source_row1-source_row0)

render_jump:
	include renderer1.asm
