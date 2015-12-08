package nahama.ofalenmod.core;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.ofalenmod.block.BlockConversionMachine;
import nahama.ofalenmod.block.BlockMachineProcessor;
import nahama.ofalenmod.block.BlockOfalen;
import nahama.ofalenmod.block.BlockOfalenOre;
import nahama.ofalenmod.block.BlockPresentBox;
import nahama.ofalenmod.block.BlockProcessorCasing;
import nahama.ofalenmod.block.BlockRepairMachine;
import nahama.ofalenmod.block.BlockSmeltingMachine;
import nahama.ofalenmod.block.BlockTeleportMarker;
import nahama.ofalenmod.itemblock.ItemOfalenBlock;
import nahama.ofalenmod.tileentity.TileEntityConversionMachine;
import nahama.ofalenmod.tileentity.TileEntityPresentBox;
import nahama.ofalenmod.tileentity.TileEntityRepairMachine;
import nahama.ofalenmod.tileentity.TileEntitySmeltingMachine;
import nahama.ofalenmod.tileentity.TileEntityTeleportMarker;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OfalenModBlockCore {

	public static Block oreOfalen;
	public static Block blockOfalen;
	public static Block machineSmelting;
	public static Block machineConversion;
	public static Block machineRepair;
	public static Block processorMachine;
	public static Block casingProcessor;
	public static Block markerTeleportOfalen;
	public static Block boxPresentOfalen;

	/** ブロックを登録する処理。 */
	public static void registerBlock() {
		oreOfalen = new BlockOfalenOre()
				.setBlockName("oreOfalen")
				.setBlockTextureName("ofalenmod:ofalen_ore-");
		GameRegistry.registerBlock(oreOfalen, ItemOfalenBlock.class, "oreOfalen");
		OreDictionary.registerOre("oreOfalen", new ItemStack(oreOfalen, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("oreOfalenRed", new ItemStack(oreOfalen, 1, 0));
		OreDictionary.registerOre("oreOfalenGreen", new ItemStack(oreOfalen, 1, 1));
		OreDictionary.registerOre("oreOfalenBlue", new ItemStack(oreOfalen, 1, 2));
		OreDictionary.registerOre("oreOfalenWhite", new ItemStack(oreOfalen, 1, 3));

		blockOfalen = new BlockOfalen()
				.setBlockName("blockOfalen")
				.setBlockTextureName("ofalenmod:ofalen_block-");
		GameRegistry.registerBlock(blockOfalen, ItemOfalenBlock.class, "blockOfalen");
		OreDictionary.registerOre("blockOfalen", new ItemStack(blockOfalen, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("blockOfalenRed", new ItemStack(blockOfalen, 1, 0));
		OreDictionary.registerOre("blockOfalenGreen", new ItemStack(blockOfalen, 1, 1));
		OreDictionary.registerOre("blockOfalenBlue", new ItemStack(blockOfalen, 1, 2));
		OreDictionary.registerOre("blockOfalenWhite", new ItemStack(blockOfalen, 1, 3));

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
	}

}
