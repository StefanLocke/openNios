.data
.align 
i :
	.word 0x50
j :
	.word 0x50

.globl _start

.text


_start:
	addi t3,zero,0
	addi t4,zero,0
	addi t5,zero,0

loop:
	beq t5,zero,add2
add1:
	addi t5,zero,0
	addi t3,t3,1
	addi t3,t3,1
	addi t3,t3,1
	
	jal endloop
add2:
	addi t5,zero,1
	addi x29,t4,1
	addi t4,t4,1
	addi t4,t4,1
endloop:
	la a0,j
	sw t4,0(a0)
	jal loop

