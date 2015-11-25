package nahama.ofalenmod.core;

import nahama.ofalenmod.block.ConversionMachine;
import nahama.ofalenmod.block.MachineProcessor;
import nahama.ofalenmod.block.OfalenBlock;
import nahama.ofalenmod.block.OfalenOre;
import nahama.ofalenmod.block.ProcessorCasing;
import nahama.ofalenmod.block.RepairMachine;
import nahama.ofalenmod.block.SmeltingMachine;
import nahama.ofalenmod.itemblock.ItemOfalenBlock;
import nahama.ofalenmod.tileentity.TileEntityConversionMachine;
import nahama.ofalenmod.tileentity.TileEntityRepairMachine;
import nahama.ofalenmod.tileentity.TileEntitySmeltingMachine;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class OfalenModBlockCore {
	//ブロックの定義
	public static Block oreOfalen;
	public static Block blockOfalen;

	//機械類の定義
	public static Block machineSmelting;
	public static Block machineConversion;
	public static Block machineRepair;

	public static Block processorMachine;
	public static Block casingProcessor;

	/**ブロックを設定する*/
	public static void registerBlock () {
		oreOfalen = new OfalenOre()
		.setBlockName("oreOfalen")
		.setBlockTextureName("ofalenmod:ofalen_ore-");
		GameRegistry.registerBlock(oreOfalen, ItemOfalenBlock.class, "oreOfalen");
		OreDictionary.registerOre("oreOfalen", new ItemStack(oreOfalen, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreOfalenRed", new ItemStack(oreOfalen, 1, 0));
		OreDictionary.registerOre("oreOfalenGreen", new ItemStack(oreOfalen, 1, 1));
		OreDictionary.registerOre("oreOfalenBlue", new ItemStack(oreOfalen, 1, 2));
		OreDictionary.registerOre("oreOfalenWhite", new ItemStack(oreOfalen, 1, 3));

		blockOfalen = new OfalenBlock()
		.setBlockName("blockOfalen")
		.setBlockTextureName("ofalenmod:ofalen_block-");
		GameRegistry.registerBlock(blockOfalen, ItemOfalenBlock.class, "blockOfalen");
		OreDictionary.registerOre("blockOfalen", new ItemStack(blockOfalen, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("blockOfalenRed", new ItemStack(blockOfalen, 1, 0));
		OreDictionary.registerOre("blockOfalenGreen", new ItemStack(blockOfalen, 1, 1));
		OreDictionary.registerOre("blockOfalenBlue", new ItemStack(blockOfalen, 1, 2));
		OreDictionary.registerOre("blockOfalenWhite", new ItemStack(blockOfalen, 1, 3));

		//機械類の設定
		machineSmelting = new SmeltingMachine()
		.setBlockName("machineSmelting")
		.setBlockTextureName("ofalenmod:smelting_machine");
		GameRegistry.registerBlock(machineSmelting, "machineSmelting");
		GameRegistry.registerTileEntity(TileEntitySmeltingMachine.class, "TileEntitySmeltingMachine");

		machineConversion = new ConversionMachine()
		.setBlockName("machineConversion")
		.setBlockTextureName("ofalenmod:conversion_machine");
		GameRegistry.registerBlock(machineConversion, "machineConversion");
		GameRegistry.registerTileEntity(TileEntityConversionMachine.class, "TileEntityConversionMachine");

		machineRepair = new RepairMachine()
		.setBlockName("machineRepair")
		.setBlockTextureName("ofalenmod:repair_machine");
		GameRegistry.registerBlock(machineRepair, "machineRepair");
		GameRegistry.registerTileEntity(TileEntityRepairMachine.class, "TileEntityRepairMachine");

		processorMachine = new MachineProcessor()
		.setBlockName("processorMachine")
		.setBlockTextureName("ofalenmod:machine_processor-");
		GameRegistry.registerBlock(processorMachine, ItemOfalenBlock.class, "processorMachine");

		casingProcessor = new ProcessorCasing()
		.setBlockName("casingProcessor")
		.setBlockTextureName("ofalenmod:processor_casing-");
		GameRegistry.registerBlock(casingProcessor,ItemOfalenBlock.class, "casingProcessor");
	}

}
