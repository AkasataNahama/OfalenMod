package nahama.ofalenmod.core;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.ofalenmod.block.*;
import nahama.ofalenmod.itemblock.ItemOfalenBlock;
import nahama.ofalenmod.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OfalenModBlockCore {

	public static Block oreOfalen;
	public static Block blockOfalen;
	public static Block machineSmelting;
	public static Block machineConversion;
	public static Block machineRepair;
	public static Block machineFusing;
	public static Block processorMachine;
	public static Block casingProcessor;
	public static Block markerTeleportOfalen;
	public static Block boxPresentOfalen;
	public static Block grassOfalen;

	/** ブロックを登録する処理。 */
	public static void registerBlock() {
		oreOfalen = new BlockOfalenOre()
				.setBlockName("oreOfalen")
				.setBlockTextureName("ofalenmod:ofalen_ore-");
		GameRegistry.registerBlock(oreOfalen, ItemOfalenBlock.class, "oreOfalen");
		OreDictionary.registerOre("oreOfalenRed", new ItemStack(oreOfalen, 1, 0));
		OreDictionary.registerOre("oreOfalenGreen", new ItemStack(oreOfalen, 1, 1));
		OreDictionary.registerOre("oreOfalenBlue", new ItemStack(oreOfalen, 1, 2));
		OreDictionary.registerOre("oreOfalenWhite", new ItemStack(oreOfalen, 1, 3));
		OreDictionary.registerOre("oreOfalen", new ItemStack(oreOfalen, 1, OreDictionary.WILDCARD_VALUE));

		blockOfalen = new BlockOfalen()
				.setBlockName("blockOfalen")
				.setBlockTextureName("ofalenmod:ofalen_block-");
		GameRegistry.registerBlock(blockOfalen, ItemOfalenBlock.class, "blockOfalen");
		OreDictionary.registerOre("blockOfalenRed", new ItemStack(blockOfalen, 1, 0));
		OreDictionary.registerOre("blockOfalenGreen", new ItemStack(blockOfalen, 1, 1));
		OreDictionary.registerOre("blockOfalenBlue", new ItemStack(blockOfalen, 1, 2));
		OreDictionary.registerOre("blockOfalenWhite", new ItemStack(blockOfalen, 1, 3));
		OreDictionary.registerOre("blockOfalenOrange", new ItemStack(blockOfalen, 1, 4));
		OreDictionary.registerOre("blockOfalenViridian", new ItemStack(blockOfalen, 1, 5));
		OreDictionary.registerOre("blockOfalenPurple", new ItemStack(blockOfalen, 1, 6));
		OreDictionary.registerOre("blockOfalenBlack", new ItemStack(blockOfalen, 1, 7));
		OreDictionary.registerOre("blockOfalen", new ItemStack(blockOfalen, 1, OreDictionary.WILDCARD_VALUE));

		machineSmelting = new BlockSmeltingMachine()
				.setBlockName("machineSmelting")
				.setBlockTextureName("ofalenmod:smelting_machine");
		GameRegistry.registerBlock(machineSmelting, "machineSmelting");
		GameRegistry.registerTileEntity(TileEntitySmeltingMachine.class, "TileEntitySmeltingMachine");

		machineConversion = new BlockConversionMachine()
				.setBlockName("machineConversion")
				.setBlockTextureName("ofalenmod:conversion_machine");
		GameRegistry.registerBlock(machineConversion, "machineConversion");
		GameRegistry.registerTileEntity(TileEntityConversionMachine.class, "TileEntityConversionMachine");

		machineRepair = new BlockRepairMachine()
				.setBlockName("machineRepair")
				.setBlockTextureName("ofalenmod:repair_machine");
		GameRegistry.registerBlock(machineRepair, "machineRepair");
		GameRegistry.registerTileEntity(TileEntityRepairMachine.class, "TileEntityRepairMachine");

		machineFusing = new BlockFusingMachine()
				.setBlockName("machineFusing")
				.setBlockTextureName("ofalenmod:fusing_machine");
		GameRegistry.registerBlock(machineFusing, "machineFusing");
		GameRegistry.registerTileEntity(TileEntityFusingMachine.class, "TileEntityFusingMachine");

		processorMachine = new BlockMachineProcessor()
				.setBlockName("processorMachine")
				.setBlockTextureName("ofalenmod:machine_processor-");
		GameRegistry.registerBlock(processorMachine, ItemOfalenBlock.class, "processorMachine");

		casingProcessor = new BlockProcessorCasing()
				.setBlockName("casingProcessor")
				.setBlockTextureName("ofalenmod:processor_casing-");
		GameRegistry.registerBlock(casingProcessor, ItemOfalenBlock.class, "casingProcessor");

		markerTeleportOfalen = new BlockTeleportMarker()
				.setBlockName("markerTeleportOfalen")
				.setBlockTextureName("ofalenmod:teleport_marker");
		GameRegistry.registerBlock(markerTeleportOfalen, "markerTeleportOfalen");
		GameRegistry.registerTileEntity(TileEntityTeleportMarker.class, "TileEntityTeleportMarker");

		boxPresentOfalen = new BlockPresentBox()
				.setBlockName("boxPresentOfalen")
				.setBlockTextureName("ofalenmod:present_box");
		GameRegistry.registerBlock(boxPresentOfalen, "boxPresentOfalen");
		GameRegistry.registerTileEntity(TileEntityPresentBox.class, "TileEntityOfalenPresentBox");

		grassOfalen = new BlockGrassOfalen()
				.setBlockName("grassOfalen")
				.setBlockTextureName("ofalenmod:ofalen_grass");
		GameRegistry.registerBlock(grassOfalen, "grassOfalen");
	}

}
