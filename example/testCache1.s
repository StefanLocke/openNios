
.globl _start

.text


_start:
	addi t0,zero,10
	sw t0,8(zero)
	addi t0,zero,10
	sw t0,0xc(zero)
	addi t0,zero,11
	sw t0,0x2c(zero)
	addi t0,zero,12
	sw t0,0x6c(zero)
	addi t0,zero,13
	sw t0,236(zero)
	
	lw t0,8(zero)
	lw t0,0xc(zero)
	lw t0,0x2c(zero)
	lw t0,0x6c(zero)
	lw t0,236(zero)
	jal _start

