/* On my honor, I have neither given nor received unauthorized aid on this assignment */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MIPSsim {
	
	static class instruction{
		int address;
		int data;
		int indexNum;
		int offset;
		int base;
		String type;
		int dest;
		int src1;
		int src2;
		int immediateValue;
		String cmd;
		
		public instruction (){
			address = 260;
			data = -1;
			indexNum = -1;
			offset = -1;
			base = -1;
			type = "";
			dest = -1;
			src1 = -1;
			src2 = -1;
			immediateValue = -1;
			cmd = "";
		}
		
		
	}

	static class FU{
		String name;
		boolean busy;
		String op;
		int dest;
		int src1;
		int src2;
		boolean ready;
		
		public FU (){
			name = "";
			busy = false;
			op = "";
			dest = -1;
			src1 = -1;
			src2 = -1;
			ready = false;
		}

	}
	
	static class register{
		int data;
		FU result;
		instruction recent;
		boolean ready;
		
		public register (){
			data = 0;
			result = new FU();
			ready = false;
			
		}

	}
	
	public static void disassembler(String[] binary, int n) {
		String category = "";
		String opcode = "";
		boolean breakFlag = false;
		
		String commands[] = new String[n];
		instruction instr[] = new instruction[n];
		for(int i = 0; i < n; i++) {
			instr[i] = new instruction();
		}
		
		int address = 260;
		int line = n;
		
		for(int i = 0; i < line; i++) {
			if(!breakFlag) {
				category = binary[i].substring(0,3);
				opcode = binary[i].substring(3,6);
				switch (category) {
					case "000" :  //cat1
						switch (opcode) {
							case "000" :  //J
								
								String instr_index = binary[i].substring(6,32);
								instr_index = instr_index + "00";
								instr[i].indexNum = Integer.parseInt(instr_index,2);
								instr[i].address = address;
								instr[i].type = "J";
								//int target = Integer.parseInt(instr_index.substring(19,22),2); //double check this
								//address = address + target;
								instr[i].cmd = "J #" + instr[i].indexNum;
								commands[i] = address + "\tJ #" + instr[i].indexNum;
								address = address + 4;
								break;
							
							case "001" :  //BEQ
								instr[i].type = "BEQ";
								instr[i].address = address;
								instr[i].src1 = Integer.parseInt(binary[i].substring(6,11),2);
								instr[i].src2 = Integer.parseInt(binary[i].substring(11,16),2);
								//instr[i].offset = Integer.parseInt(binary[i].substring(16,32) + "00",2);
								if((binary[i].substring(16,32).charAt(0) == '1')){
									instr[i].offset = Integer.parseUnsignedInt("1111111111111111" + binary[i].substring(16,32) + "00",2);
								}
								else {
									instr[i].offset = Integer.parseInt(binary[i].substring(16,32) + "00",2);
								}
								instr[i].cmd = "BEQ " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2 + ", #" + instr[i].offset;
								commands[i] = address + "\tBEQ " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2 + ", #" + instr[i].offset;
								address = address + 4;
								break;
							
							case "010" :  //BNE					
								instr[i].type = "BNE";
								instr[i].address = address;
								instr[i].src1 = Integer.parseInt(binary[i].substring(6,11),2);
								instr[i].src2 = Integer.parseInt(binary[i].substring(11,16),2);
								//instr[i].offset = Integer.parseInt(binary[i].substring(16,32) + "00",2);
								if((binary[i].substring(16,32).charAt(0) == '1')){
									instr[i].offset = Integer.parseUnsignedInt("1111111111111111" + binary[i].substring(16,32) + "00",2);
								}
								else {
									instr[i].offset = Integer.parseInt(binary[i].substring(16,32) + "00",2);
								}
								instr[i].cmd = "BNE " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2 + ", #" + instr[i].offset;
								commands[i] = address + "\tBNE " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2 + ", #" + instr[i].offset;
								address = address + 4;
								break;
							
							case "011" :  //BGTZ						
								instr[i].type = "BGTZ";
								instr[i].address = address;
								instr[i].src1 = Integer.parseInt(binary[i].substring(6,11),2);
								//instr[i].offset = Integer.parseInt(binary[i].substring(16,32) + "00",2);
								if((binary[i].substring(16,32).charAt(0) == '1')){
									instr[i].offset = Integer.parseUnsignedInt("1111111111111111" + binary[i].substring(16,32) + "00",2);
								}
								else {
									instr[i].offset = Integer.parseInt(binary[i].substring(16,32) + "00",2);
								}
								instr[i].cmd = "BGTZ " + "R" + instr[i].src1 + ", #" + instr[i].offset;
								commands[i] = address + "\tBGTZ " + "R" + instr[i].src1 + ", #" + instr[i].offset;
								address = address + 4;
								break;
							
							case "100" : //SW
								instr[i].type = "SW";
								instr[i].address = address;
								instr[i].base = Integer.parseInt(binary[i].substring(6,11),2);
								instr[i].dest = Integer.parseInt(binary[i].substring(11,16),2);
								//instr[i].offset = Integer.parseInt(binary[i].substring(16,32),2);
								if((binary[i].substring(16,32).charAt(0) == '1')){
									instr[i].offset = Integer.parseUnsignedInt("1111111111111111" + binary[i].substring(16,32),2);
								}
								else {
									instr[i].offset = Integer.parseInt(binary[i].substring(16,32),2);
								}
								instr[i].cmd = "SW R" + instr[i].dest + ", " + instr[i].offset + "(R" + instr[i].base + ")";
								commands[i] = address + "\tSW R" + instr[i].dest + ", " + instr[i].offset + "(R" + instr[i].base + ")";
								address = address + 4;
								break;
							
							case "101" :  //LW
								instr[i].type = "LW";
								instr[i].address = address;
								instr[i].base = Integer.parseInt(binary[i].substring(6,11),2);
								instr[i].dest = Integer.parseInt(binary[i].substring(11,16),2);
								//instr[i].offset = Integer.parseInt(binary[i].substring(16,32),2);
								if((binary[i].substring(16,32).charAt(0) == '1')){
									instr[i].offset = Integer.parseUnsignedInt("1111111111111111" + binary[i].substring(16,32),2);
								}
								else {
									instr[i].offset = Integer.parseInt(binary[i].substring(16,32),2);
								}
								instr[i].cmd = "LW R" + instr[i].dest + ", " + instr[i].offset + "(R" + instr[i].base + ")";
								commands[i] = address + "\tLW R" + instr[i].dest + ", " + instr[i].offset + "(R" + instr[i].base + ")";
								address = address + 4;
								break;
							
							case "110" :  //BREAK
								breakFlag = true;
								instr[i].type = "BREAK";
								instr[i].address = address;
								instr[i].cmd = "BREAK";
								commands[i] = address + "\tBREAK";
								address = address + 4;
								break;
							
						}
						break;
					
					case "001" :  //cat2
						instr[i].dest = Integer.parseInt(binary[i].substring(6,11),2);
						instr[i].src1 = Integer.parseInt(binary[i].substring(11,16),2);
						instr[i].src2 = Integer.parseInt(binary[i].substring(16,21),2);
						switch (opcode) {
							case "000" :  //ADD
								instr[i].type = "ADD";
								instr[i].address = address;
								instr[i].cmd = "ADD " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2;
								commands[i] = address + "\tADD " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2;
								address = address + 4;
								break;
							
							case "001" :  //SUB
								instr[i].type = "SUB";
								instr[i].address = address;
								instr[i].cmd = "SUB " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2;
								commands[i] = address + "\tSUB " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2;
								address = address + 4;
								break;
							
							case "010" :  //AND
								instr[i].type = "AND";
								instr[i].address = address;
								instr[i].cmd = "AND " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2;
								commands[i] = address + "\tAND " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2;
								address = address + 4;
								break;
							
							case "011" :  //OR
								instr[i].type = "OR";
								instr[i].address = address;
								instr[i].cmd = "OR " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2;
								commands[i] = address + "\tOR " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2;
								address = address + 4;
								break;
							
							case "100" :  //SRL
								instr[i].type = "SRL";
								instr[i].address = address;
								instr[i].src2 = Integer.parseInt(binary[i].substring(16,21),2);
								instr[i].cmd = "SRL " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "#" + instr[i].src2;
								commands[i] = address + "\tSRL " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "#" + instr[i].src2;
								address = address + 4;
								break;
							
							case "101" :  //SRA
								instr[i].type = "SRA";
								instr[i].address = address;
								instr[i].src2 = Integer.parseInt(binary[i].substring(16,21),2);
								instr[i].cmd = "SRA " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "#" + instr[i].src2;
								commands[i] = address + "\tSRA " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "#" + instr[i].src2;
								address = address + 4;
								break;
							
							case "110" :  //MUL
								instr[i].type = "MUL";
								instr[i].address = address;
								instr[i].cmd = "MUL " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2;
								commands[i] = address + "\tMUL " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "R" + instr[i].src2;
								address = address + 4;
								break;
							
							
						}
						break;
					
					case "010" : //cat3
						instr[i].dest = Integer.parseInt(binary[i].substring(6,11),2);
						instr[i].src1 = Integer.parseInt(binary[i].substring(11,16),2);
						
						switch (opcode) {
							case "000" :  //ADDI
								if((binary[i].substring(16,32).charAt(0) == '1')){
									instr[i].immediateValue = Integer.parseUnsignedInt("1111111111111111"  +binary[i].substring(16,32),2);
								}
								else {
									instr[i].immediateValue = Integer.parseInt(binary[i].substring(16,32),2);
								}
								instr[i].type = "ADDI";
								instr[i].address = address;
								instr[i].cmd = "ADDI " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "#" + instr[i].immediateValue;
								commands[i] = address + "\tADDI " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "#" + instr[i].immediateValue;
								address = address + 4;
								break;
							
							case "001" :  //ANDI
								instr[i].immediateValue = Integer.parseInt(binary[i].substring(16,32),2);
								instr[i].type = "ANDI";
								instr[i].address = address;
								instr[i].cmd = "ANDI " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "#" + instr[i].immediateValue;
								commands[i] = address + "\tANDI " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "#" + instr[i].immediateValue;
								address = address + 4;
								break;
							
							case "010" :  //ORI
								instr[i].immediateValue = Integer.parseInt(binary[i].substring(16,32),2);
								instr[i].type = "ORI";
								instr[i].address = address;
								instr[i].cmd = "ORI " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "#" + instr[i].immediateValue;
								commands[i] = address + "\tORI " + "R" + instr[i].dest + ", " + "R" + instr[i].src1 + ", " + "#" + instr[i].immediateValue;
								address = address + 4;
								break;
							
							
						
					}
					break;
				}
			}
			else {
				instr[i].type = "DATA";
				instr[i].address = address;
				instr[i].data = Integer.parseUnsignedInt(binary[i],2);
				commands[i] = address + "\t" + instr[i].data;
				address = address + 4;
			}
		}
		
		FileWriter writer = null;
		try {
			writer = new FileWriter("disassembly.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      for (int i = 0; i < n; i++) {
	         try {
				writer.write(binary[i] + "\t" + commands[i] + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
	      try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	      pipeline(instr, commands, n);
	}
		


	public static void pipeline(instruction[] instr, String[] commands, int n) {
		
		instruction waiting = new instruction();
		instruction excecuted = new instruction();
		int cycle = 1;
		int temp = 0;
		int j = 0;
		int address = 260;
		register registers[] = new register[32];
		int memsize = 0;
		instruction mem[] = new instruction[1];
		boolean breakFlag = false;
		boolean branchFlag = false;
		
		FileWriter writer = null;
		try {
			writer = new FileWriter("simulation.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int meme = 0;
		for(int i = 0; i < n; i++) {
			if(instr[i].type.equals("BREAK")) {
				memsize = n - i - 1;
				mem = new instruction[memsize];
			}
			if(instr[i].type.equals("DATA")) {
				mem[meme] = instr[i];
				meme++;
			}
		}
		
		
		for(int i = 0; i < 32; i++) {
			registers[i] = new register();
		}
		
		instruction[] buf1 = new instruction[8];
		for(int b = 0; b < 8; b++) {
			buf1[b] = new instruction();
		}
		instruction[] buf2 = new instruction[2];
		buf2[0] = new instruction();
		buf2[1] = new instruction();
		instruction[] sbuf2 = new instruction[2];
		sbuf2[0] = new instruction();
		sbuf2[1] = new instruction();
		instruction[] buf3 = new instruction[2];
		buf3[0] = new instruction();
		buf3[1] = new instruction();
		instruction[] sbuf3 = new instruction[2];
		sbuf3[0] = new instruction();
		sbuf3[1] = new instruction();
		instruction[] buf4 = new instruction[2];
		buf4[0] = new instruction();
		buf4[1] = new instruction();
		instruction[] sbuf4 = new instruction[2];
		sbuf4[0] = new instruction();
		sbuf4[1] = new instruction();
		instruction buf5 = new instruction();
		instruction buf6 = new instruction();
		instruction buf7 = new instruction();
		instruction buf8 = new instruction();
		instruction buf9 = new instruction();
		instruction buf10 = new instruction();
		
		FU MEM = new FU();
		FU WB = new FU();
		
		
		
		
		while(!breakFlag){
			
			instruction mul3i = new instruction();
			instruction alu2i = new instruction();
			instruction alu1i = new instruction();
			instruction mul2i = new instruction();
			instruction mul1i = new instruction();
			instruction memi = new instruction();
			branchFlag = false;
			
			sbuf2[0] = buf2[0];
			sbuf2[1] = buf2[1];
			sbuf3[0] = buf3[0];
			sbuf3[1] = buf3[1];
			sbuf4[0] = buf4[0];
			sbuf4[1] = buf4[1];
			
			if(cycle > 2) {
				//MUL1
				if(buf4[0].type.equals("MUL")) {
					mul1i = buf4[0];
					buf4[0] = buf4[1];
					buf4[1] = new instruction();
					
				}
				
				//MUL2
				if(buf7.type.equals("MUL")) {
					mul2i = buf7;
				}
				
				//ALU2
				if(buf3[0].type.equals("ADD")) {
					alu2i = buf3[0];
					alu2i.data = registers[buf3[0].src1].data + registers[buf3[0].src2].data;
					buf3[0] = buf3[1];
					buf3[1] = new instruction();
					
				}
				else if(buf3[0].type.equals("SUB")) {
					alu2i = buf3[0];
					alu2i.data = registers[buf3[0].src1].data - registers[buf3[0].src2].data;
					buf3[0] = buf3[1];
					buf3[1] = new instruction();
					
				}
				else if(buf3[0].type.equals("AND")) {
					alu2i = buf3[0];
					alu2i.data = registers[buf3[0].src1].data & registers[buf3[0].src2].data;
					buf3[0] = buf3[1];
					buf3[1] = new instruction();
					
				}
				else if(buf3[0].type.equals("OR")) {
					alu2i = buf3[0];
					alu2i.data = registers[buf3[0].src1].data | registers[buf3[0].src2].data;
					buf3[0] = buf3[1];
					buf3[1] = new instruction();
					
				}
				else if(buf3[0].type.equals("SRL")) {
					alu2i = buf3[0];
					alu2i.data = registers[buf3[0].src1].data >>> registers[buf3[0].src2].data;
					buf3[0] = buf3[1];
					buf3[1] = new instruction();
					
				}
				else if(buf3[0].type.equals("SRA")) {
					alu2i = buf3[0];
					alu2i.data = registers[buf3[0].src1].data >> registers[buf3[0].src2].data;
					buf3[0] = buf3[1];
					buf3[1] = new instruction();
					
				}
				else if(buf3[0].type.equals("ADDI")) {
					alu2i = buf3[0];
					alu2i.data = registers[buf3[0].src1].data + buf3[0].immediateValue;
					buf3[0] = buf3[1];
					buf3[1] = new instruction();
					
				}
				else if(buf3[0].type.equals("ANDI")) {
					alu2i = buf3[0];
					alu2i.data = registers[buf3[0].src1].data & buf3[0].immediateValue;
					buf3[0] = buf3[1];
					buf3[1] = new instruction();
					
				}
				else if(buf3[0].type.equals("ORI")) {
					alu2i = buf3[0];
					alu2i.data = registers[buf3[0].src1].data | buf3[0].immediateValue;
					buf3[0] = buf3[1];
					buf3[1] = new instruction();
					
				}
				
				//ALU1
				if(buf2[0].type.equals("SW")) {
					alu1i = buf2[0];
					alu1i.data = registers[buf2[0].base].data + buf2[0].offset;
					buf2[0] = buf2[1];
					buf2[1] = new instruction();
					
				}
				else if(buf2[0].type.equals("LW")) {
					alu1i = buf2[0];
					alu1i.data = registers[buf2[0].base].data + buf2[0].offset;
					buf2[0] = buf2[1];
					buf2[1] = new instruction();
					
				}
			}
			
			if(cycle > 3) {
				//MUL2
				if(buf7.type.equals("MUL")) {
					mul2i = buf7;
					buf7 = mul1i;
				}
				
				//MEM
				if(buf5.type.equals("SW")) {
					for(int sw = 0; sw < memsize; sw++) {
						if(mem[sw].address == buf5.data) {
							mem[sw].data = registers[buf5.dest].data;
							if(registers[buf5.dest].recent == buf5) {
								registers[buf5.dest].result.ready = true;
							}
						}
					}
					buf5 = alu1i;
					
				}
				else if(buf5.type.equals("LW")) {
					memi = buf5;
					for(int lw = 0; lw < memsize; lw++) {
						if(mem[lw].address == buf5.data) {
							memi.data = mem[lw].data;
						}
					}
					buf5 = alu1i;
					
				}

			}
			
			if(cycle > 4) {
				//MUL3
				if(buf9.type.equals("MUL")) {
					mul3i = buf9;
					mul3i.data = registers[buf9.src1].data * registers[buf9.src2].data;
					buf9 = new instruction();
					
				}
			}
			
			boolean store = false;
			instruction[] issuing = new instruction[8];
			for(int b = 0; b < 8; b++) {
				issuing[b] = new instruction();
			}
			int ic = 0;
			if(cycle > 1) {
				//Issue Unit
				for(int is = 0; is < 8; is++) {
					boolean raw = false;
					boolean waw = false;
					boolean war = false;
					boolean struct = false;
					boolean between = false;
					
					
					if(buf1[is].type=="") {
						break;
					}
					
					//check for RAW hazards
					
					for(int comp = is - 1; comp >= 0; comp--) {
						if(buf1[is].src1 != -1 && buf1[is].src1 == buf1[comp].dest) {
							raw = true;
						}
						else if(buf1[is].src2 != -1 && buf1[is].src2 == buf1[comp].dest) {
							raw = true;
						}
						else if(buf1[is].base != -1 && buf1[is].base == buf1[comp].dest){
							raw = true;
						}
					}
					
					if(buf1[is].src1 != -1) {
						if(buf1[is].src1 == buf2[0].dest || buf1[is].src1 == buf2[1].dest || buf1[is].src1 == buf3[0].dest || buf1[is].src1 == buf3[1].dest || buf1[is].src1 == buf4[0].dest || buf1[is].src1 == buf4[1].dest || buf1[is].src1 == buf5.dest || buf1[is].src1 == buf6.dest || buf1[is].src1 == buf7.dest || buf1[is].src1 == buf8.dest || buf1[is].src1 == buf9.dest || buf1[is].src1 == buf10.dest) {
							raw = true;
						}
					}
					
					if(buf1[is].src2 != -1) {
						if(buf1[is].src2 == buf2[0].dest || buf1[is].src2 == buf2[1].dest || buf1[is].src2 == buf3[0].dest || buf1[is].src2 == buf3[1].dest || buf1[is].src2 == buf4[0].dest || buf1[is].src2 == buf4[1].dest || buf1[is].src2 == buf5.dest || buf1[is].src2 == buf6.dest || buf1[is].src2 == buf7.dest || buf1[is].src2 == buf8.dest || buf1[is].src2 == buf9.dest || buf1[is].src2 == buf10.dest) {
							raw = true;
						}
					}
					
					if(buf1[is].base != -1) {
						if(buf1[is].base == buf2[0].dest || buf1[is].base == buf2[1].dest || buf1[is].base == buf3[0].dest || buf1[is].base == buf3[1].dest || buf1[is].base == buf4[0].dest || buf1[is].base == buf4[1].dest || buf1[is].base == buf5.dest || buf1[is].base == buf6.dest || buf1[is].base == buf7.dest || buf1[is].base == buf8.dest || buf1[is].base == buf9.dest || buf1[is].base == buf10.dest) {
							raw = true;
						}
					}
					
					//check for WAW hazards
					if(buf1[is].dest == buf2[0].dest || buf1[is].dest == buf2[1].dest || buf1[is].dest == buf3[0].dest || buf1[is].dest == buf3[1].dest || buf1[is].dest == buf4[0].dest || buf1[is].dest == buf4[1].dest || buf1[is].dest == buf5.dest || buf1[is].dest == buf6.dest || buf1[is].dest == buf7.dest || buf1[is].dest == buf8.dest || buf1[is].dest == buf9.dest || buf1[is].dest == buf10.dest) {
						waw = true;
					}
					for(int comp = is - 1; comp >= 0; comp--) {
						if(buf1[is].dest == buf1[comp].dest) {
							waw = true;
						}
					}
					
					//check for WAR hazards
					for(int comp = is - 1; comp >= 0; comp--) {
						if(is == 0) {
							break;
						}
						if(buf1[is].dest == buf1[comp].src1 || buf1[is].dest == buf1[comp].src2 || buf1[is].dest == buf1[comp].base) {
							war = true;
						}
					}
					switch(buf1[is].type) {
						case "SW" :  
							if(!sbuf2[0].type.equals("") && !sbuf2[1].type.equals("")) {
								struct = true;
							}
							break;
						
						case "LW" : { 
							if(!sbuf2[0].type.equals("") && !sbuf2[1].type.equals("")) {
								struct = true;
							}
							break;
						}
						case "MUL" :  
							if(!sbuf4[0].type.equals("") && !sbuf4[1].type.equals("")) {
								struct = true;
							}
							break;
						
						default :  
							if(!sbuf3[0].type.equals("") && !sbuf3[1].type.equals("")) {
								struct = true;
							}
							break;
					}
					
					if(!raw && !waw && !war && !struct) {
						issuing[ic] = buf1[is];

						for(int comp = ic - 1; comp >= 0; comp--) {
							if(issuing[ic].dest == issuing[comp].dest || issuing[ic].dest == issuing[comp].src1 || issuing[ic].dest == issuing[comp].src2 || issuing[ic].dest == issuing[comp].base) {
								between = true;
							}
						}
						ic++;
						if (between == false){
							switch(buf1[is].type) {
								//add in special condisitons for issuing load and store commands
								case "SW" :
									if (registers[buf1[is].base].ready && registers[buf1[is].dest].ready && is == 0) {
										store = true;
										registers[buf1[is].dest].result = MEM;
										registers[buf1[is].dest].ready = false;
										registers[buf1[is].dest].recent = buf1[is];
										if(buf2[0].type.equals("")) {
											buf2[0] = buf1[is];
											buf1[is] = new instruction();
										}
										else if(buf2[1].type.equals("")) {
											buf2[1] = buf1[is];
											buf1[is] = new instruction();
										}
									}
									break;
								
								case "LW" : {
									if (store == false && registers[buf1[is].base].ready) {
										registers[buf1[is].dest].result = WB;
										registers[buf1[is].dest].ready = false;
										registers[buf1[is].dest].recent = buf1[is];
										if(buf2[0].type.equals("")) {
											buf2[0] = buf1[is];
											buf1[is] = new instruction();
										}
										else if(buf2[1].type.equals("")) {
											buf2[1] = buf1[is];
											buf1[is] = new instruction();
										}
									}
									break;
								}
								case "MUL" :
									registers[buf1[is].dest].result = WB;
									registers[buf1[is].dest].ready = false;
									registers[buf1[is].dest].recent = buf1[is];
									if(buf4[0].type.equals("")) {
										buf4[0] = buf1[is];
										buf1[is] = new instruction();
									}
									else if(buf4[1].type.equals("")) {
										buf4[1] = buf1[is];
										buf1[is] = new instruction();
									}
									break;
								
								default :
									registers[buf1[is].dest].result = WB;
									registers[buf1[is].dest].ready = false;
									registers[buf1[is].dest].recent = buf1[is];
									if(buf3[0].type.equals("")) {
										buf3[0] = buf1[is];
										buf1[is] = new instruction();
									}
									else if(buf3[1].type.equals("")) {
										buf3[1] = buf1[is];
										buf1[is] = new instruction();
									}
									break;
							}
						}
					}
				}
				
				//shifts b1 up, needs work
				int shift = 0;
				for(int b1 = 0; b1 < 8; b1++) {
					if(!buf1[b1].type.equals("")) {
						buf1[shift++] = buf1[b1];
					}
				}
				
				while (shift < 8) {
					buf1[shift++] = new instruction();
				}
			}
			
			//IF
			int i = 0;
			for(i = temp; i < temp + 4; i++) {
				switch(waiting.type) {
					case "BEQ" :
						if(registers[waiting.src1].ready && registers[waiting.src2].ready) {
							excecuted = waiting;
							waiting = new instruction();
							address = address + 4;
							if(registers[excecuted.src1].data == registers[excecuted.src2].data) {
								address = address + excecuted.offset;
								for(j = 0; j < n; j++) {
									if(instr[j].address == address) {
										temp = j;
										//temp++;
										branchFlag = true;
										break;
									}
								}
							
							}
						}
						
					break;
					case "BNE" :
						if(registers[waiting.src1].ready && registers[waiting.src2].result.ready) {
							excecuted = waiting;
							waiting = new instruction();
							address = address + 4;
							if(registers[excecuted.src1].data != registers[excecuted.src2].data) {
								address = address + excecuted.offset;
								for(j = 0; j < n; j++) {
									if(instr[j].address == address) {
										temp = j;
										branchFlag = true;
										break;
									}
								}
							
							}
						}
						
					break;
					
					case "BGTZ" :
						if(registers[instr[i].src1].ready) {
							excecuted = waiting;
							waiting = new instruction();
							address = address + 4;
							if(registers[excecuted.src1].data > 0) {
								address = address + excecuted.offset;
								for(j = 0; j < n; j++) {
									if(instr[j].address == address) {
										temp = j;
										branchFlag = true;
										break;
									}
								}
							}
						}
						
						break;
					default :
						break;
					}
				
				if(!waiting.type.equals("") || !excecuted.type.equals("") ) {
					break;
				}
				if(!buf1[0].type.equals("") && !buf1[1].type.equals("") && !buf1[2].type.equals("") && !buf1[3].type.equals("") && !buf1[4].type.equals("") && !buf1[5].type.equals("") && !buf1[6].type.equals("") && !buf1[7].type.equals("")) {
					temp = i;
					break;
				}
				switch(instr[i].type) {
					case "J" :
						excecuted = instr[i];
						waiting = new instruction();
						for(j = 0; j < n; j++) {
							if(instr[j].address == instr[i].indexNum) {
								address = instr[i].indexNum;
								temp = j;
								branchFlag = true;
								break;
							}
						}
						break;
					
					case "BEQ" :
						if(registers[instr[i].src1].ready && registers[instr[i].src1].ready) {
							excecuted = instr[i];
							waiting = new instruction();
							address = address + 4;
							if(registers[instr[i].src1].data == registers[instr[i].src2].data) {
								address = address + instr[i].offset;
								for(j = 0; j < n; j++) {
									if(instr[j].address == address) {
										temp = j;
										//temp++;
										branchFlag = true;
										break;
									}
								}
							
							}
						}
						else {
							waiting = instr[i];
						}
						
					break;
					case "BNE" :
						if(registers[instr[i].src1].ready && registers[instr[i].src1].ready) {
							excecuted = instr[i];
							waiting = new instruction();
							address = address + 4;
							if(registers[instr[i].src1].data != registers[instr[i].src2].data) {
								address = address + instr[i].offset;
								for(j = 0; j < n; j++) {
									if(instr[j].address == address) {
										temp = j;
										//temp++;
										branchFlag = true;
										break;
									}
								}
							
							}
						}
						else {
							waiting = instr[i];
						}
						
					break;
					
					case "BGTZ" :
						if(registers[instr[i].src1].ready) {
							excecuted = instr[i];
							waiting = new instruction();
							address = address + 4;
							if(registers[instr[i].src1].data > 0) {
								address = address + instr[i].offset;
								for(j = 0; j < n; j++) {
									if(instr[j].address == address) {
										temp = j;
										//temp++;
										branchFlag = true;
										break;
									}
								}
							}
						}
						else {
							waiting = instr[i];
						}
						
						break;
						
					case "BREAK" :
						excecuted = instr[i];
						breakFlag = true;
						break;
						
					default :
						address = address + 4;
						if(buf1[0].type.equals("")){
							buf1[0] = instr[i];
						}
						else if(buf1[1].type.equals("")){
							buf1[1] = instr[i];
						}
						else if(buf1[2].type.equals("")){
							buf1[2] = instr[i];
						}
						else if(buf1[3].type.equals("")){
							buf1[3] = instr[i];
						}
						else if(buf1[4].type.equals("")){
							buf1[4] = instr[i];
						}
						else if(buf1[5].type.equals("")){
							buf1[5] = instr[i];
						}
						else if(buf1[6].type.equals("")){
							buf1[6] = instr[i];
						}
						else if(buf1[7].type.equals("")){
							buf1[7] = instr[i];
						}
					}
				

			}
			
			if(branchFlag == false) {
				temp = i;
			}
			
	
			
			
			if(cycle > 3) {
				//WB
				if(!buf8.type.equals("")) {
					registers[buf8.dest].data = buf8.data;
					if(registers[buf8.dest].recent == buf8) {
						registers[buf8.dest].ready = true;
					}
					buf8 = memi;
				}
				if(!buf6.type.equals("")) {
					registers[buf6.dest].data = buf6.data;
					if(registers[buf6.dest].recent == buf6) {
						registers[buf6.dest].ready = true;
					}
					buf6 = alu2i;
				}
				if(!buf10.type.equals("")) {
					registers[buf10.dest].data = buf10.data;
					if(registers[buf10.dest].recent == buf10) {
						registers[buf10.dest].ready = true;
					}
					buf10 = mul3i;
				}
			}
			if(!mul3i.type.equals("")) {
				buf10 = mul3i;
			}
			if(!alu2i.type.equals("")) {
				buf6 = alu2i;
			}
			if(!memi.type.equals("")) {
				buf8 = memi;
			}
			if(!alu1i.type.equals("")) {
				buf5 = alu1i;
			}
			if(!alu2i.type.equals("")) {
				buf6 = alu2i;
			}
			if(!mul1i.type.equals("")) {
				buf7 = mul1i;
			}
			if(!mul2i.type.equals("")) {
				buf9 = mul2i;
			}
			
			
			
			try {
				writer.write("--------------------\n");
				writer.write("Cycle " + cycle  +":\n\n");
				writer.write("IF:\n");
				if(!waiting.cmd.equals("")) {
					writer.write("\tWaiting: [" + waiting.cmd + "]\n");
				}
				else {
					writer.write("\tWaiting:\n");
				}
				if(!excecuted.cmd.equals("")) {
					writer.write("\tExecuted: [" + excecuted.cmd + "]\n");
				}
				else {
					writer.write("\tExecuted:\n");
				}
				writer.write("Buf1:\n");
				if(!buf1[0].cmd.equals("")) {
					writer.write("\tEntry 0: [" + buf1[0].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 0:\n");
				}
				if(!buf1[1].cmd.equals("")) {
					writer.write("\tEntry 1: [" + buf1[1].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 1:\n");
				}
				if(!buf1[2].cmd.equals("")) {
					writer.write("\tEntry 2: [" + buf1[2].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 2:\n");
				}
				if(!buf1[3].cmd.equals("")) {
					writer.write("\tEntry 3: [" + buf1[3].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 3:\n");
				}
				if(!buf1[4].cmd.equals("")) {
					writer.write("\tEntry 4: [" + buf1[4].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 4:\n");
				}
				if(!buf1[5].cmd.equals("")) {
					writer.write("\tEntry 5: [" + buf1[5].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 5:\n");
				}
				if(!buf1[6].cmd.equals("")) {
					writer.write("\tEntry 6: [" + buf1[6].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 6:\n");
				}
				if(!buf1[7].cmd.equals("")) {
					writer.write("\tEntry 7: [" + buf1[7].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 7:\n");
				}
				writer.write("Buf2:\n");
				if(!buf2[0].cmd.equals("")) {
					writer.write("\tEntry 0: [" + buf2[0].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 0:\n");
				}
				if(!buf2[1].cmd.equals("")) {
					writer.write("\tEntry 1: [" + buf2[1].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 1:\n");
				}
				writer.write("Buf3:\n");
				if(!buf3[0].cmd.equals("")) {
					writer.write("\tEntry 0: [" + buf3[0].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 0:\n");
				}
				if(!buf3[1].cmd.equals("")) {
					writer.write("\tEntry 1: [" + buf3[1].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 1:\n");
				}
				writer.write("Buf4:\n");
				if(!buf4[0].cmd.equals("")) {
					writer.write("\tEntry 0: [" + buf4[0].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 0:\n");
				}
				if(!buf4[1].cmd.equals("")) {
					writer.write("\tEntry 1: [" + buf4[1].cmd + "]\n");
				}
				else {
					writer.write("\tEntry 1:\n");
				}
				if(!buf5.cmd.equals("")) {
					writer.write("Buf5: [" + buf5.cmd + "]\n");
				}
				else {
					writer.write("Buf5:\n");
				}
				if(!buf6.cmd.equals("")) {
					writer.write("Buf6: [" + buf6.data + ", R" + buf6.dest + "]\n");
				}	
				else{
					writer.write("Buf6:\n");
				}
				if(!buf7.cmd.equals("")) {
					writer.write("Buf7: [" + buf7.cmd + "]\n");
				}
				else {
					writer.write("Buf7:\n");
				}
				if(!buf8.cmd.equals("")) {
					writer.write("Buf8: [" + buf8.data + ", R" + buf8.dest + "]\n");
				}				
				else{
					writer.write("Buf8:\n");
				}
				if(!buf9.cmd.equals("")) {
					writer.write("Buf9: [" + buf9.cmd + "]\n");
				}
				else {
					writer.write("Buf9:\n");
				}
				if(!buf10.cmd.equals("")) {
					writer.write("Buf10: [" + buf10.data + ", R" + buf10.dest + "]\n\n");
				}
				else{
					writer.write("Buf10:\n\n");
				}
					
				writer.write("Registers\n");
				writer.write("R00:\t" + registers[0].data + "\t" + registers[1].data  + "\t" + registers[2].data  + "\t" + registers[3].data  + "\t" + registers[4].data  + "\t" + registers[5].data  + "\t" + registers[6].data  + "\t" + registers[7].data  + "\n");
				writer.write("R08:\t" + registers[8].data  + "\t" + registers[9].data  + "\t" + registers[10].data  + "\t" + registers[11].data  + "\t" + registers[12].data  + "\t" + registers[13].data  + "\t" + registers[14].data  + "\t" + registers[15].data  + "\n");
				writer.write("R16:\t" + registers[16].data  + "\t" + registers[17].data  + "\t" + registers[18].data  + "\t" + registers[19].data  + "\t" + registers[20].data  + "\t" + registers[21].data  + "\t" + registers[22].data  + "\t" + registers[23].data  + "\n");
				writer.write("R24:\t" + registers[24].data  + "\t" + registers[25].data  + "\t" + registers[26].data  + "\t" + registers[27].data  + "\t" + registers[28].data  + "\t" + registers[29].data  + "\t" + registers[30].data  + "\t" + registers[31].data  + "\n");
				writer.write("\nData");
				int memtemp = 0;
				for(int k = 0; k < memsize; k++) {
					if(memtemp % 8 == 0 || memtemp == 0) {
						writer.write("\n" + mem[k].address + ":");
					}
					memtemp++;
					writer.write("\t" + mem[k].data);
					
				}
				writer.write("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!excecuted.type.equals("")) {
				excecuted = new instruction();
			}
			cycle++;
			
	
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	public static void simulator(instruction[] instr, String[] commands, int n) {
		int register[] = new int[32];
		int cycle = 1;
		int i, j = 0;
		int temp = 0;
		int address = 260;
		boolean breakFlag = false;
		boolean jumpFlag = false;
		boolean branchFlag = false;
		
		FileWriter writer = null;
		try {
			writer = new FileWriter("simulation.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(i = 0; i < 32; i++) {
			register[i] = 0;
		}
		i = 0;
		while(!breakFlag && i < n){
			jumpFlag = false;
			branchFlag = false;
			if(instr[i].address == address) {
				switch(instr[i].type) {
					case "J" : 
						for(j = 0; j < n; j++) {
							if(instr[j].address == instr[i].indexNum) {
								address = instr[i].indexNum;
								jumpFlag = true;
								break;
							}
						}
						break;
					
					case "BEQ" : 
						if(register[instr[i].src1] == register[instr[i].src2]) {
							address = address + instr[i].offset;
							for(j = 0; j < n; j++) {
								if(instr[j].address == address) {
									branchFlag = true;
									break;
								}
							}
						
					}
					break;
					case "BNE" : 
						if(register[instr[i].src1] != register[instr[i].src2]) {
							address = address + instr[i].offset;
							for(j = 0; j < n; j++) {
								if(instr[j].address == address) {
									branchFlag = true;
									break;
								}
							}
						}
						break;
					
					case "BGTZ" :  
						if(register[instr[i].src1] > 0) {
							address = address + instr[i].offset;
							for(j = 0; j < n; j++) {
								if(instr[j].address == address) {
									branchFlag = true;
									break;
								}
							}
						}
						break;
					
					case "SW" :  
						for(j = 0; j < n; j++) {
							if(instr[j].address == register[instr[i].base] + instr[i].offset) {
								instr[j].data = register[instr[i].dest];
							}
						}
						break;
					
					case "LW" : { 
						for(j = 0; j < n; j++) {
							if(instr[j].address == register[instr[i].base] + instr[i].offset) {
								register[instr[i].dest] = instr[j].data;
							}
						}
						break;
					}
					case "ADD" :  
						register[instr[i].dest] = register[instr[i].src1] + register[instr[i].src2];
						break;
					
					case "SUB" :  
						register[instr[i].dest] = register[instr[i].src1] - register[instr[i].src2];
						break;
					
					case "AND" : 
						register[instr[i].dest] = register[instr[i].src1] & register[instr[i].src2];
						break;
					
					case "OR" : 
						register[instr[i].dest] = register[instr[i].src1] | register[instr[i].src2];
						break;
					
					case "SRL" : 
						register[instr[i].dest] = register[instr[i].src1] >>> register[instr[i].src2];
						break;
					
					case "SRA" : 
						register[instr[i].dest] = register[instr[i].src1] >> register[instr[i].src2];
							break;
					
					case "MUL" : 
						register[instr[i].dest] = register[instr[i].src1] * register[instr[i].src2];
						break;
					
					case "ADDI" : 
						register[instr[i].dest] = register[instr[i].src1] + instr[i].immediateValue;
						break;
					
					case "ANDI" : 
						register[instr[i].dest] = register[instr[i].src1] & instr[i].immediateValue;
						break;
					
					case "ORI" :  
						register[instr[i].dest] = register[instr[i].src1] | instr[i].immediateValue;
						break;
					
					case "BREAK" : 
						breakFlag = true;
						break;
					
					
					
				}
			}
			
			try {
				writer.write("--------------------\n");
				writer.write("Cycle " + cycle + ":\t" + commands[i] + "\n\n");
				writer.write("Registers\n");
				writer.write("R00:\t" + register[0] + "\t" + register[1] + "\t" + register[2] + "\t" + register[3] + "\t" + register[4] + "\t" + register[5] + "\t" + register[6] + "\t" + register[7] + "\n");
				writer.write("R08:\t" + register[8] + "\t" + register[9] + "\t" + register[10] + "\t" + register[11] + "\t" + register[12] + "\t" + register[13] + "\t" + register[14] + "\t" + register[15] + "\n");
				writer.write("R16:\t" + register[16] + "\t" + register[17] + "\t" + register[18] + "\t" + register[19] + "\t" + register[20] + "\t" + register[21] + "\t" + register[22] + "\t" + register[23] + "\n");
				writer.write("R24:\t" + register[24] + "\t" + register[25] + "\t" + register[26] + "\t" + register[27] + "\t" + register[28] + "\t" + register[29] + "\t" + register[30] + "\t" + register[31] + "\n");
				writer.write("\nData");
				for(int k = 0; k < n; k++) {
					
					if(instr[k].type.equals("DATA")) {
						if(temp % 8 == 0 || temp == 0) {
							writer.write("\n" + instr[k].address + ":");
						}
						temp++;
						writer.write("\t" + instr[k].data);
					}
				}
				writer.write("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (jumpFlag) {
				i = j;
			}
			else if (branchFlag) {
				i = j;
				i++;
				address = address + 4;
			}
			else {
				i++;
				address = address + 4;
			}
			cycle++;
			
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		File textFile = new File("sample.txt");
		//File textFile = new File(args[0]);
        
        int n = 0;
        int i = 0;
		Scanner scanner = new Scanner(textFile);
        while (scanner.hasNextLine()) {
        	scanner.nextLine();
            n++;
        }
        scanner.close();
        String input[] = new String[n];
        scanner = new Scanner(textFile);
        while (scanner.hasNext()) {
        	input[i] = scanner.next();
            i++;
        }
        scanner.close();
        
        
        disassembler(input, n);  
	}
}
