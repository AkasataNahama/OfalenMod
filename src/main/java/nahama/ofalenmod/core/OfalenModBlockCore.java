package nahama.ofalenmod.core;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.ofalenmod.block.*;
import nahama.ofalenmod.item.block.ItemBlockWorldEditorBase;
import nahama.ofalenmod.item.block.ItemOfalenBlock;
import nahama.ofalenmod.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class OfalenModBlockCore {
	// 基本ブロック
	public static Block oreOfalen;
	public static Block blockOfalen;
	// 機械系
	public static Block machineSmelting;
	public static Block machineConverting;
	public static Block machineRepairing;
	public static Block machineFusing;
	public static Block processor;
	public static Block casingProcessor;
	// 世界系
	public static Block placerOfalen;
	public static Block moverOfalen;
	public static Block breakerOfalen;
	public static Block blockCollectorOfalen;
	public static Block surveyorOfalen;
	// その他
	public static Block markerTeleporting;
	public static Block boxPresent;
	public static Block grassOfalen;
	public static Block setterDetailed;

	/** ブロックを登録する。 */
	public static void registerBlock() {
		// オファレン鉱石
		oreOfalen = new BlockOfalenOre().setBlockName("ofalen.ore").setBlockTextureName("ofalenmod:ore");
		GameRegistry.registerBlock(oreOfalen, ItemOfalenBlock.class, "oreOfalen");
		OreDictionary.registerOre("oreOfalenRed", new ItemStack(oreOfalen, 1, 0));
		OreDictionary.registerOre("oreOfalenGreen", new ItemStack(oreOfalen, 1, 1));
		OreDictionary.registerOre("oreOfalenBlue", new ItemStack(oreOfalen, 1, 2));
		OreDictionary.registerOre("oreOfalenWhite", new ItemStack(oreOfalen, 1, 3));
		OreDictionary.registerOre("oreOfalen", new ItemStack(oreOfalen, 1, OreDictionary.WILDCARD_VALUE));
		// オファレンブロック
		blockOfalen = new BlockOfalen().setBlockName("ofalen.block").setBlockTextureName("ofalenmod:block");
		GameRegistry.registerBlock(blockOfalen, ItemOfalenBlock.class, "blockOfalen");
		OreDictionary.registerOre("blockOfalenRed", new ItemStack(blockOfalen, 1, 0));
		OreDictionary.registerOre("blockOfalenGreen", new ItemStack(blockOfalen, 1, 1));
		OreDictionary.registerOre("blockOfalenBlue", new ItemStack(blockOfalen, 1, 2));
		OreDictionary.registerOre("blockOfalenWhite", new ItemStack(blockOfalen, 1, 3));
		OreDictionary.registerOre("blockOfalenOrange", new ItemStack(blockOfalen, 1, 4));
		OreDictionary.registerOre("blockOfalenViridian", new ItemStack(blockOfalen, 1, 5));
		OreDictionary.registerOre("blockOfalenPurple", new ItemStack(blockOfalen, 1, 6));
		OreDictionary.registerOre("blockOfalenDark", new ItemStack(blockOfalen, 1, 7));
		OreDictionary.registerOre("blockOfalen", new ItemStack(blockOfalen, 1, OreDictionary.WILDCARD_VALUE));
		// 製錬機
		machineSmelting = new BlockMachineSmelting().setBlockName("ofalen.machineSmelting").setBlockTextureName("ofalenmod:smelting_machine");
		GameRegistry.registerBlock(machineSmelting, "machineSmelting");
		GameRegistry.registerTileEntityWithAlternatives(TileEntitySmeltingMachine.class, "OfalenMod.SmeltingMachine", "TileEntitySmeltingMachine");
		// 変換機
		machineConverting = new BlockMachineConverting().setBlockName("ofalen.machineConverting").setBlockTextureName("ofalenmod:converting_machine");
		GameRegistry.registerBlock(machineConverting, "machineConversion");
		GameRegistry.registerTileEntityWithAlternatives(TileEntityConvertingMachine.class, "OfalenMod.ConvertingMachine", "TileEntityConversionMachine");
		// 修繕機
		machineRepairing = new BlockMachineRepairing().setBlockName("ofalen.machineRepairing").setBlockTextureName("ofalenmod:repairing_machine");
		GameRegistry.registerBlock(machineRepairing, "machineRepair");
		GameRegistry.registerTileEntityWithAlternatives(TileEntityRepairingMachine.class, "OfalenMod.RepairingMachine", "TileEntityRepairMachine");
		// 融合機
		machineFusing = new BlockMachineFusing().setBlockName("ofalen.machineFusing").setBlockTextureName("ofalenmod:fusing_machine");
		GameRegistry.registerBlock(machineFusing, "machineFusing");
		GameRegistry.registerTileEntity(TileEntityFusingMachine.class, "OfalenMod.FusingMachine");
		// 処理装置
		processor = new BlockProcessor().setBlockName("ofalen.processor").setBlockTextureName("ofalenmod:processor");
		GameRegistry.registerBlock(processor, ItemOfalenBlock.class, "processorMachine");
		// 処理装置筐体
		casingProcessor = new BlockProcessorCasing().setBlockName("ofalen.casingProcessor").setBlockTextureName("ofalenmod:processor_casing");
		GameRegistry.registerBlock(casingProcessor, ItemOfalenBlock.class, "casingProcessor");
		// 設置機
		placerOfalen = new BlockWorldEditorBase() {
			@Override
			public TileEntity createNewTileEntity(World world, int meta) {
				return new TileEntityPlacer();
			}
		}.setBlockName("ofalen.placer").setBlockTextureName("ofalenmod:placer");
		GameRegistry.registerBlock(placerOfalen, ItemBlockWorldEditorBase.class, "placer");
		GameRegistry.registerTileEntity(TileEntityPlacer.class, "OfalenMod.Placer");
		// 移動機
		moverOfalen = new BlockWorldEditorBase() {
			@Override
			public TileEntity createNewTileEntity(World world, int meta) {
				return new TileEntityMover();
			}
		}.setBlockName("ofalen.mover").setBlockTextureName("ofalenmod:mover");
		GameRegistry.registerBlock(moverOfalen, ItemBlockWorldEditorBase.class, "mover");
		GameRegistry.registerTileEntity(TileEntityMover.class, "OfalenMod.Mover");
		// 移動機
		breakerOfalen = new BlockWorldEditorBase() {
			@Override
			public TileEntity createNewTileEntity(World world, int meta) {
				return new TileEntityBreaker();
			}
		}.setBlockName("ofalen.breaker").setBlockTextureName("ofalenmod:breaker");
		GameRegistry.registerBlock(breakerOfalen, ItemBlockWorldEditorBase.class, "breaker");
		GameRegistry.registerTileEntity(TileEntityBreaker.class, "OfalenMod.Breaker");
		// 収集機
		blockCollectorOfalen = new BlockWorldEditorBase() {
			@Override
			public TileEntity createNewTileEntity(World world, int meta) {
				return new TileEntityCollector();
			}
		}.setBlockName("ofalen.collector").setBlockTextureName("ofalenmod:collector");
		GameRegistry.registerBlock(blockCollectorOfalen, ItemBlockWorldEditorBase.class, "collector_block");
		GameRegistry.registerTileEntity(TileEntityCollector.class, "OfalenMod.Collector");
		// 測量器
		surveyorOfalen = new BlockSurveyor().setBlockName("ofalen.surveyor").setBlockTextureName("ofalenmod:surveyor");
		GameRegistry.registerBlock(surveyorOfalen, "surveyor");
		// テレポートマーカー
		markerTeleporting = new BlockTeleportingMarker().setBlockName("ofalen.markerTeleporting").setBlockTextureName("ofalenmod:teleporting_marker");
		GameRegistry.registerBlock(markerTeleporting, "markerTeleportOfalen");
		GameRegistry.registerTileEntityWithAlternatives(TileEntityTeleportingMarker.class, "OfalenMod.TeleportingMarker", "TileEntityTeleportMarker");
		// プレゼントボックス
		boxPresent = new BlockPresentBox().setBlockName("ofalen.boxPresent").setBlockTextureName("ofalenmod:present_box");
		GameRegistry.registerBlock(boxPresent, "boxPresentOfalen");
		GameRegistry.registerTileEntityWithAlternatives(TileEntityPresentBox.class, "OfalenMod.PresentBox", "TileEntityOfalenPresentBox");
		// オファレン草
		grassOfalen = new BlockOfalenGrass().setBlockName("ofalen.grass").setBlockTextureName("ofalenmod:grass");
		GameRegistry.registerBlock(grassOfalen, "grassOfalen");
		// 詳細設定器
		setterDetailed = new BlockDetailedSetter().setBlockName("ofalen.setterDetailed").setBlockTextureName("ofalenmod:detailed_setter");
		GameRegistry.registerBlock(setterDetailed, "setterDetailed");
		GameRegistry.registerTileEntity(TileEntityDetailedSetter.class, "OfalenMod.DetailedSetter");
	}
}
