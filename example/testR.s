main:
	addi t1,zero,0
	addi t2,zero,0
	addi t3,zero,0

loop:
	beq t3,zero,add2
add1:
	addi t3,zero,0
	addi t1,t1,1
	jal endloop
add2:
	addi t3,zero,1
	addi t2,t2,1
endloop:
	jal loop
	