
.text
main:
	addi t1,zero,1
	addi t2,zero,-1
	addi t3,zero,-1

loop: 
	slli t1,t1,1
	srli t2,t2,1
	srai t3,t3,1
	jal loop

	