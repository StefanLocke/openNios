package elfParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.operations.And;

import openSim.gui.command.systemLevel.CommandCompileCode;
import riscvSimulator.RegisterFileRiscV;
import riscvSimulator.RiscVMemory;
import riscvSimulator.values.RiscVValue32;
import riscvSimulator.values.RiscVValue8;

public class ElfFileParser {
	

	
	public ElfFileParser(RiscVMemory memory,RegisterFileRiscV registers){
		byte[] elfBytes=null;
		try {
			elfBytes = Files.readAllBytes(Paths.get("./",CommandCompileCode.LinkedElf));
			System.out.println("Elf Size :" + elfBytes.length);
		} catch (IOException e) {
			System.err.println("Error while loading elf file !");
			e.printStackTrace();
		}
		
		
		registers.set(2, new RiscVValue32(0xf000));
		byte instructionSize = elfBytes[4];
		byte endianness = elfBytes[5];
		byte abi = elfBytes[7];
		
		
		
		long placeOfSectionTable = toUnsignedInt(elfBytes[32], elfBytes[33], elfBytes[34], elfBytes[35]);
		long sizeOfSectionHeader = toUnsignedShort(elfBytes[46], elfBytes[47]);
		long numberSections = toUnsignedShort(elfBytes[48], elfBytes[49]);
		long indexSymbolTable = toUnsignedShort(elfBytes[50], elfBytes[51]);
		ArrayList<String> names = new ArrayList<String>();
		long startOfSymbolTable = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + indexSymbolTable * sizeOfSectionHeader + 16)], elfBytes[(int) (placeOfSectionTable + indexSymbolTable * sizeOfSectionHeader + 17)], elfBytes[(int) (placeOfSectionTable + indexSymbolTable * sizeOfSectionHeader + 18)], elfBytes[(int) (placeOfSectionTable + indexSymbolTable * sizeOfSectionHeader + 19)]);
		int indexElf=(int) startOfSymbolTable, indexSection=0;

		while (indexSection<numberSections){
			//We build string corresponding to section name
			String newString = "";
			while (elfBytes[(int) (indexElf)] != 0){
				newString += (char) elfBytes[indexElf];
				indexElf++;
			}
			names.add(newString);
			indexElf++;
			indexSection++;
			
		}
		long destination = 0;
		long idOfText = -1;
		long idOfData = -1;

		//On current version of the code, we will only write .text and .data sections on memory
		//We start by writing .text
		for (int sectionNumber=0; sectionNumber< numberSections; sectionNumber++){
				
				long indexOfName = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 1)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 2)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 3)]);
				
				if (getStringFrom(indexOfName+startOfSymbolTable, elfBytes).equals(".text")){
				
				idOfText = indexOfName;
					
				long startOfTextSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 16)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 17)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 18)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 19)]);
				
				long sizeOfSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 20)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 21)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 22)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 23)]);
				
				long addressOfSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 12)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 13)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 14)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 15)]);
				
				System.out.println("Text start : " + Long.toHexString(startOfTextSection));
				System.out.println("Text size : " + Long.toHexString(sizeOfSection));
				System.out.println("Text address : " + Long.toHexString(addressOfSection));

				destination = addressOfSection;
				registers.setPC(new RiscVValue32(addressOfSection));
				for (long byteNumber=startOfTextSection; byteNumber<startOfTextSection+sizeOfSection; byteNumber++){
					memory.storeByte((int)destination, new RiscVValue8(toUnsignedChar(elfBytes[(int) byteNumber])), false);
					destination++;
				}
				
			}
		}

		long startOfDataSection = destination;
		//Then we do the same for data section
		for (int sectionNumber=0; sectionNumber< numberSections; sectionNumber++){
			
			long indexOfName = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 1)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 2)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 3)]);
			
			if (getStringFrom(indexOfName+startOfSymbolTable, elfBytes).equals(".data")){
			
				idOfData = indexOfName;
				
				long startOfTextSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 16)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 17)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 18)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 19)]);
				
				long sizeOfSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 20)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 21)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 22)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 23)]);
				
				long addressOfSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 12)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 13)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 14)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 15)]);
				
				System.out.println("data start : " + Long.toHexString(startOfTextSection));
				System.out.println("data size : " + Long.toHexString(sizeOfSection));
				System.out.println("data address : " + Long.toHexString(addressOfSection));
				destination = addressOfSection;
				for (long byteNumber=startOfTextSection; byteNumber<startOfTextSection+sizeOfSection; byteNumber++){
					memory.storeByte((int)destination, new RiscVValue8(toUnsignedChar(elfBytes[(int) byteNumber])), false);
					destination++;
				}
			}
			
			
			
		}
		long strTabStart = 0 ;
		for (int sectionNumber=0; sectionNumber< numberSections; sectionNumber++){
			long indexOfName = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 1)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 2)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 3)]);
			
			if (getStringFrom(indexOfName+startOfSymbolTable, elfBytes).equals(".strtab")) {
				strTabStart = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 16)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 17)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 18)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 19)]);
				System.out.println("str tab starts at " + Long.toHexString(strTabStart));
			}
		}
		
		
		//We go find the stack pointer address
		for (int sectionNumber=0; sectionNumber< numberSections; sectionNumber++){
			
			long indexOfName = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 4)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 5)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 6)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 7)]);
			System.out.println("Section : " + sectionNumber +" Header id : " + Long.toHexString(indexOfName));
			
			if (indexOfName == 0x2) {
				System.out.println("We are in the SymbTab header");
				
				long tabsymbStart = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 16)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 17)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 18)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 19)]);
				System.out.println("The SymbTab is at " + Long.toHexString(tabsymbStart));
				
				long tabsymbsize = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 20)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 21)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 22)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 23)]);
			
				long actualSize = tabsymbsize/16;
				System.out.println("The SymbTab size is " + tabsymbsize + " with " + actualSize + " entries");
				for (int index = 0 ; index < actualSize; index++) {
					long name = toUnsignedInt(elfBytes[(int) (tabsymbStart + index * 16)], 
							elfBytes[(int) (tabsymbStart + index * 16) + 1 ], 
							elfBytes[(int) (tabsymbStart + index * 16) + 2 ], 
							elfBytes[(int) (tabsymbStart + index * 16) + 3 ]);
					
					
					long value = toUnsignedInt(elfBytes[(int) (tabsymbStart + index * 16) + 4], 
							elfBytes[(int) (tabsymbStart + index * 16) + 5 ], 
							elfBytes[(int) (tabsymbStart + index * 16) + 6 ], 
							elfBytes[(int) (tabsymbStart + index * 16) + 7 ]);
					
					if (getStringFrom(name+strTabStart, elfBytes).equals("__global_pointer$")) {
						registers.set(3, new RiscVValue32(value));
					}
					System.out.println("--Symbol : " + index + " value : " + Long.toHexString(value) + " name : " + getStringFrom(name+strTabStart, elfBytes));
				}
				
				
				

			/*
			long tabsymbStart =toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 16)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 17)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 18)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 19)]);
					/*
					toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0x10)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0x11)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0x12)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0x13)])
			
			
			long tabsumbsize = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0x1C)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0x1d)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0x1e)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0x1f)]);
			
			System.out.println("The SymbTab is at " + Long.toHexString(tabsumbsize));
			System.out.println("The SymbTab size is " + tabsumbsize);
			int symbIndex = 0;
			long symbAddress = tabsymbStart;
			*/
			
			
			}
			
			
			
			
			
		}
		
		//We handle relocation of .text section
		/*for (int sectionNumber=0; sectionNumber< numberSections; sectionNumber++){
			
			long indexOfName = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 0)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 1)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 2)], 
					elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 3)]);
			
			if (getStringFrom(indexOfName+startOfSymbolTable, elfBytes).equals(".rela.text")){
				
				long startOfRelaSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 16)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 17)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 18)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 19)]);
				
				long sizeOfSection = toUnsignedInt(elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 20)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 21)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 22)], 
						elfBytes[(int) (placeOfSectionTable + sectionNumber * sizeOfSectionHeader + 23)]);
				
	
				
				for (long relaNumber = 0; relaNumber<sizeOfSection; relaNumber+=12){
					long relocationAddress = toUnsignedInt(elfBytes[(int) (startOfRelaSection + relaNumber + 0)], elfBytes[(int) (startOfRelaSection + relaNumber + 1)], elfBytes[(int) (startOfRelaSection + relaNumber + 2)], elfBytes[(int) (startOfRelaSection + relaNumber + 3)]);
					long relocationInfo = toUnsignedInt(elfBytes[(int) (startOfRelaSection + relaNumber + 4)], elfBytes[(int) (startOfRelaSection + relaNumber + 5)], elfBytes[(int) (startOfRelaSection + relaNumber + 6)], elfBytes[(int) (startOfRelaSection + relaNumber + 7)]);
					long relocationType = relocationInfo & 0xff;
					long relocationValue = toUnsignedInt(elfBytes[(int) (startOfRelaSection + relaNumber + 8)], elfBytes[(int) (startOfRelaSection + relaNumber + 9)], elfBytes[(int) (startOfRelaSection + relaNumber + 10)], elfBytes[(int) (startOfRelaSection + relaNumber + 11)]);
					long relocationSection = (relocationInfo >>8) & 0xff;
					if (relocationSection == 1) {
						if (idOfText<idOfData)
							relocationValue += 0;
						else
							relocationValue += startOfDataSection;
					}
					else if (relocationSection == 2) {
						if (idOfText>idOfData)
							relocationValue += 0;
						else
							relocationValue += startOfDataSection;					}
					else {
						System.out.println("Unsupported relocation to a section which is not data or text  !");
					}
					

					
					System.out.println("Relocation type is " + relocationType);
					if (relocationType == 11){
						//This is a %hiadj relocation
						long value = ((relocationValue >> 16) & 0xffff) + ((relocationValue >> 15) & 0x01);
						value = value << 6; //We align the value with the immediate field of IType expressions
						
						long instruction = memory.loadWord(relocationAddress).getUnsignedValue(); //This is the previous instruction
						instruction = instruction | value; //We add the immediate value
						memory.setWord(relocationAddress, new RiscVValue32(instruction));
						
					}
					else if (relocationType == 10){
						//This is a %lo relocation
						long value = relocationValue & 0xffff;
						value = value << 6; //We align the value with the immediate field of IType expressions
						
						long instruction = memory.loadWord(relocationAddress).getUnsignedValue(); //This is the previous instruction
						instruction = instruction | value; //We add the immediate value

						memory.setWord(relocationAddress, new RiscVValue32(instruction));
					}
					else if (relocationType == 3){
						//This is a %pcrel16 relocation
						long value = relocationValue & 0xffff;
						value = value - relocationAddress;
						value = value << 6; //We align the value with the immediate field of IType expressions
						
						long instruction = memory.loadWord(relocationAddress).getUnsignedValue(); //This is the previous instruction
						instruction = instruction | value; //We add the immediate value

						memory.setWord(relocationAddress, new RiscVValue32(instruction));
					}
					
					System.out.println("Relocating (type " + Long.toHexString(relocationType)+ ") at address 0x" + Long.toHexString(relocationAddress) + " value 0x" + Long.toHexString(relocationValue));
					
				}
			}
		}*/

	}
	
	private long toUnsignedInt(byte byte1, byte byte2, byte byte3, byte byte4){
		long v1 = ((long) byte1) & 0xff;
		long v2 = ((long) byte2) & 0xff;
		long v3 = ((long) byte3) & 0xff;
		long v4 = ((long) byte4) & 0xff;
		
		return v1+ (v2<<8) + (v3<<16) + (v4<<24);
		
	}
	
	private long toUnsignedShort(byte byte1, byte byte2){
		long v1 = ((long) byte1) & 0xff;
		long v2 = ((long) byte2) & 0xff;
;
		
		return v1+ (v2<<8);
		
	}
	
	private long toUnsignedChar(byte byte1){
		long v1 = ((long) byte1) & 0xff;

		
		return v1;
		
	}
	
	private String getStringFrom(long byteOffset, byte[] elfBytes){
		String result = "";
		while (elfBytes[(int) byteOffset] != 0){
			result += (char) elfBytes[(int) byteOffset];
			byteOffset++;
		}
		return result;
	}
}
