bitmap_ function y, bitmap+(y>>3)*320+(y&7)

lda_ffff_op = $ad
jmp_ffff_op = $4c

plotterGroupBegin macro gi

plotter_gr{"\{gi}"} label *
	jmp plotter_gr_end{"\{gi}"}

prev_an1 set 1000
prev_an2 set 1000
prev_offs set 1000

	endm

plotterGroupBegin0 macro gi

plotter_gr{"\{gi}"} label *
	jmp plotter_gr_end{"\{gi}"}

prev_an1 set 1000
prev_an2 set 1000
prev_offs set 1000

	endm

plotterGroupBegin1 macro gi

plotter_gr{"\{gi}"} label *
	lda plotter_gr_end{"\{gi}"}

prev_an1 set 1000
prev_an2 set 1000
prev_offs set 1000

	endm


plotterGroupEnd macro gi

plotter_gr_end{"\{gi}"} label *

	endm

plottersEnd macro

	rts

	endm

renderer4Ang macro ang

render_{"\{ang}"} label *

	endm

renderer4AngDefaults macro ang, firstGr

render_{"\{ang}"} label plotter_gr{"\{firstGr}"}

	endm

enableGroup macro
	lda #lo(lda_ffff_op)
	endm

disableGroup macro
	lda #lo(jmp_ffff_op)
	endm

updateGroup macro gi
	sta plotter_gr{"\{gi}"}
	endm

callRender macro firstGr
	jmp plotter_gr{"\{firstGr}"}
	endm

plotter macro coltb,yc,ang1,ang2,offset

	if coltb==color_
		plotBm yc,ang1,ang2,offset
	else
		plotColor coltb,yc,ang1,ang2,offset
	endif
	
	endm

	
plotColor macro coltb,yc,ang1,ang2,offset

color set coltb
	if color==color12
	  if (yc>>1)&1
color set color21
	  endif
	endif

	if (ang1<>prev_an1) || (ang2<>prev_an2) || (offset<>prev_offs)

	lda sintab+ang1
	if offset>0
	adc #lo(2*offset)
	endif
	if offset<0
	adc #lo(abs(255+2*(offset)))
	endif
	adc sintab+ang2
	tay
	and #$f8
	tax
	
	endif
	
	lda bitmap_(yc),x
	if color<>color3
	and mask,y
        endif
	if color<>color0
	ora color,y
        endif
	sta bitmap_(yc),x
	
prev_an1 set ang1
prev_an2 set ang2
prev_offs set offset

	endm

plotBm macro yc,ang1,ang2,offset

	if (ang1<>prev_an1) || (ang2<>prev_an2) || (offset<>prev_offs)

	lda sintab+ang1
	if offset>0
	adc #lo(2*offset)
	endif
	if offset<0
	adc #lo(abs(255+2*(offset)))
	endif
	adc sintab+ang2
	tay
	and #$f8
	tax

	endif

	lda bitmap_(yc)-bitmap+source,x
	and color3,y
	sta $ff
	lda bitmap_(yc),x
	and mask,y
	ora $ff
	sta bitmap_(yc),x

prev_an1 set ang1
prev_an2 set ang2
prev_offs set offset

	endm
