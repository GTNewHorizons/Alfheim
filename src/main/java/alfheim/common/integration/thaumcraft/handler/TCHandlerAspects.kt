package alfheim.common.integration.thaumcraft.handler

import alfheim.common.core.registry.AlfheimBlocks.alfheimPortal
import alfheim.common.core.registry.AlfheimBlocks.alfheimPylon
import alfheim.common.core.registry.AlfheimBlocks.animatedTorch
import alfheim.common.core.registry.AlfheimBlocks.anomaly
import alfheim.common.core.registry.AlfheimBlocks.anyavil
import alfheim.common.core.registry.AlfheimBlocks.dreamLeaves
import alfheim.common.core.registry.AlfheimBlocks.dreamLog
import alfheim.common.core.registry.AlfheimBlocks.dreamSapling
import alfheim.common.core.registry.AlfheimBlocks.elvenOres
import alfheim.common.core.registry.AlfheimBlocks.elvenSand
import alfheim.common.core.registry.AlfheimBlocks.elvoriumBlock
import alfheim.common.core.registry.AlfheimBlocks.itemHolder
import alfheim.common.core.registry.AlfheimBlocks.livingcobble
import alfheim.common.core.registry.AlfheimBlocks.manaInfuser
import alfheim.common.core.registry.AlfheimBlocks.mauftriumBlock
import alfheim.common.core.registry.AlfheimBlocks.poisonIce
import alfheim.common.core.registry.AlfheimBlocks.redFlame
import alfheim.common.core.registry.AlfheimBlocks.tradePortal
import alfheim.common.core.registry.AlfheimItems
import alfheim.common.core.registry.AlfheimItems.astrolabe
import alfheim.common.core.registry.AlfheimItems.auraRingElven
import alfheim.common.core.registry.AlfheimItems.auraRingGod
import alfheim.common.core.registry.AlfheimItems.cloudPendant
import alfheim.common.core.registry.AlfheimItems.cloudPendantSuper
import alfheim.common.core.registry.AlfheimItems.creativeReachPendant
import alfheim.common.core.registry.AlfheimItems.dodgeRing
import alfheim.common.core.registry.AlfheimItems.elementalBoots
import alfheim.common.core.registry.AlfheimItems.elementalChestplate
import alfheim.common.core.registry.AlfheimItems.elementalHelmet
import alfheim.common.core.registry.AlfheimItems.elementalHelmetRevealing
import alfheim.common.core.registry.AlfheimItems.elementalLeggings
import alfheim.common.core.registry.AlfheimItems.elementiumHoe
import alfheim.common.core.registry.AlfheimItems.elfFirePendant
import alfheim.common.core.registry.AlfheimItems.elfIcePendant
import alfheim.common.core.registry.AlfheimItems.elvenResource
import alfheim.common.core.registry.AlfheimItems.elvoriumBoots
import alfheim.common.core.registry.AlfheimItems.elvoriumChestplate
import alfheim.common.core.registry.AlfheimItems.elvoriumHelmet
import alfheim.common.core.registry.AlfheimItems.elvoriumHelmetRevealing
import alfheim.common.core.registry.AlfheimItems.elvoriumLeggings
import alfheim.common.core.registry.AlfheimItems.excaliber
import alfheim.common.core.registry.AlfheimItems.flugelDisc
import alfheim.common.core.registry.AlfheimItems.flugelSoul
import alfheim.common.core.registry.AlfheimItems.invisibilityCloak
import alfheim.common.core.registry.AlfheimItems.livingrockPickaxe
import alfheim.common.core.registry.AlfheimItems.lootInterceptor
import alfheim.common.core.registry.AlfheimItems.manaRingElven
import alfheim.common.core.registry.AlfheimItems.manaRingGod
import alfheim.common.core.registry.AlfheimItems.manaStone
import alfheim.common.core.registry.AlfheimItems.manaStoneGreater
import alfheim.common.core.registry.AlfheimItems.manasteelHoe
import alfheim.common.core.registry.AlfheimItems.mask
import alfheim.common.core.registry.AlfheimItems.paperBreak
import alfheim.common.core.registry.AlfheimItems.peacePipe
import alfheim.common.core.registry.AlfheimItems.pixieAttractor
import alfheim.common.core.registry.AlfheimItems.realitySword
import alfheim.common.core.registry.AlfheimItems.rodFire
import alfheim.common.core.registry.AlfheimItems.rodGrass
import alfheim.common.core.registry.AlfheimItems.rodIce
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule.alfheimThaumOre
import net.minecraft.item.ItemStack
import thaumcraft.api.ThaumcraftApi
import thaumcraft.api.aspects.*
import vazkii.botania.common.item.ModItems.manaResource

object TCHandlerAspects {
	
	fun addAspects() {
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 0), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("metallum"), 2).add(Aspect.getAspect("permutatio"), 2).add(Aspect.getAspect("venenum"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 7), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("vinculum"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 1), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("aer"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 2), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("ignis"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 3), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("aqua"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 4), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("terra"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 5), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("ordo"), 3).add(Aspect.getAspect("vitreus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimThaumOre, 1, 6), AspectList().add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("perditio"), 3).add(Aspect.getAspect("vitreus"), 2))
		
		ThaumcraftApi.registerObjectTag(ItemStack(elvenOres, 1, 0), AspectList().add(Aspect.getAspect("praecantatio"), 1).add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("lucrum"), 3).add(Aspect.getAspect("vitreus"), 3))    // dragonstone
		ThaumcraftApi.registerObjectTag(ItemStack(elvenOres, 1, 1), AspectList().add(Aspect.getAspect("praecantatio"), 1).add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("metallum"), 3))                                                // elementium
		ThaumcraftApi.registerObjectTag(ItemStack(elvenOres, 1, 2), AspectList().add(Aspect.getAspect("praecantatio"), 1).add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("vitreus"), 3))                                                // quartz
		ThaumcraftApi.registerObjectTag(ItemStack(elvenOres, 1, 3), AspectList().add(Aspect.getAspect("praecantatio"), 1).add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("lucrum"), 1).add(Aspect.getAspect("metallum"), 2))    // gold
		ThaumcraftApi.registerObjectTag(ItemStack(elvenOres, 1, 4), AspectList().add(Aspect.getAspect("praecantatio"), 1).add(Aspect.getAspect("terra"), 1).add(Aspect.getAspect("sensus"), 3))                                                // iffesal
		ThaumcraftApi.registerObjectTag(ItemStack(livingcobble), AspectList().add(Aspect.getAspect("perditio"), 1).add(Aspect.getAspect("terra"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(elvenSand), AspectList().add(Aspect.getAspect("perditio"), 1).add(Aspect.getAspect("terra"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(dreamLog), AspectList().add(Aspect.getAspect("arbor"), 4).add(Aspect.getAspect("herba"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(dreamLeaves), AspectList().add(Aspect.getAspect("herba"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(dreamSapling), AspectList().add(Aspect.getAspect("arbor"), 1).add(Aspect.getAspect("herba"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(redFlame), AspectList().add(Aspect.getAspect("ignis"), 16).add(Aspect.getAspect("perditio"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(poisonIce), AspectList().add(Aspect.getAspect("gelum"), 16).add(Aspect.getAspect("ordo"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(manaInfuser), AspectList().add(Aspect.getAspect("fabrico"), 5).add(Aspect.getAspect("metallum"), 10).add(Aspect.getAspect("ordo"), 4).add(Aspect.getAspect("permutatio"), 4).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("terra"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimPortal), AspectList().add(Aspect.getAspect("auram"), 2).add(Aspect.getAspect("arbor"), 8).add(Aspect.getAspect("iter"), 4).add(Aspect.getAspect("metallum"), 4).add(Aspect.getAspect("praecantatio"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(tradePortal), AspectList().add(Aspect.getAspect("iter"), 4).add(Aspect.getAspect("metallum"), 1).add(Aspect.getAspect("praecantatio"), 4).add(Aspect.getAspect("terra"), 6))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumBlock), AspectList().add(Aspect.getAspect("alienis"), 6).add(Aspect.getAspect("lucrum"), 20).add(Aspect.getAspect("metallum"), 27).add(Aspect.getAspect("praecantatio"), 54))
		ThaumcraftApi.registerObjectTag(ItemStack(mauftriumBlock), AspectList().add(Aspect.getAspect("auram"), 64).add(Aspect.getAspect("alienis"), 64).add(Aspect.getAspect("lucrum"), 64).add(Aspect.getAspect("metallum"), 54).add(Aspect.getAspect("potentia"), 64).add(Aspect.getAspect("praecantatio"), 64))
		ThaumcraftApi.registerObjectTag(ItemStack(anyavil), AspectList().add(Aspect.getAspect("lucrum"), 10).add(Aspect.getAspect("metallum"), 46).add(Aspect.getAspect("praecantatio"), 52))
		ThaumcraftApi.registerObjectTag(ItemStack(animatedTorch), AspectList().add(Aspect.getAspect("motus"), 1).add(Aspect.getAspect("potentia"), 1).add(Aspect.getAspect("machina"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(itemHolder), AspectList().add(Aspect.getAspect("terra"), 4).add(Aspect.getAspect("potentia"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(anomaly), AspectList().add(Aspect.getAspect("alienis"), 8).add(Aspect.getAspect("potentia"), 2).add(Aspect.getAspect("perditio"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimPylon, 1, 0), AspectList().add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimPylon, 1, 1), AspectList().add(Aspect.getAspect("auram"), 2).add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("potentia"), 2).add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(alfheimPylon, 1, 2), AspectList().add(Aspect.getAspect("spiritus"), 2).add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("potentia"), 6).add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("vitreus"), 4).add(Aspect.getAspect("alienis"), 3))
		
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 0), AspectList().add(Aspect.getAspect("alienis"), 8).add(Aspect.getAspect("iter"), 4).add(Aspect.getAspect("lux"), 8).add(Aspect.getAspect("ordo"), 8).add(Aspect.getAspect("praecantatio"), 8))                                                // Intercore
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 1), AspectList().add(Aspect.getAspect("lucrum"), 4).add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("potentia"), 4).add(Aspect.getAspect("praecantatio"), 6))                                                                                            // Infuscore
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 2), AspectList().add(Aspect.getAspect("alienis"), 1).add(Aspect.getAspect("lucrum"), 3).add(Aspect.getAspect("metallum"), 4).add(Aspect.getAspect("praecantatio"), 8))                                                                                            // Elvorium
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 3), AspectList().add(Aspect.getAspect("auram"), 20).add(Aspect.getAspect("alienis"), 16).add(Aspect.getAspect("lucrum"), 20).add(Aspect.getAspect("metallum"), 8).add(Aspect.getAspect("praecantatio"), 50).add(Aspect.getAspect("potentia"), 16))    // Mauftrium
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 4), AspectList().add(Aspect.getAspect("auram"), 10).add(Aspect.getAspect("alienis"), 8).add(Aspect.getAspect("lucrum"), 10).add(Aspect.getAspect("metallum"), 4).add(Aspect.getAspect("praecantatio"), 20).add(Aspect.getAspect("ignis"), 24))    // Fire ingot
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 5), AspectList().add(Aspect.getAspect("auram"), 10).add(Aspect.getAspect("alienis"), 8).add(Aspect.getAspect("lucrum"), 10).add(Aspect.getAspect("metallum"), 4).add(Aspect.getAspect("praecantatio"), 20).add(Aspect.getAspect("gelum"), 24))    // Ice ingot
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 6), AspectList().add(Aspect.getAspect("lucrum"), 1).add(Aspect.getAspect("metallum"), 1).add(Aspect.getAspect("praecantatio"), 2))                                                                                                                                        // Elv nugg
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 7), AspectList().add(Aspect.getAspect("auram"), 2).add(Aspect.getAspect("alienis"), 2).add(Aspect.getAspect("lucrum"), 2).add(Aspect.getAspect("metallum"), 1).add(Aspect.getAspect("praecantatio"), 5).add(Aspect.getAspect("potentia"), 2))    // Mau nugg
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 8), AspectList().add(Aspect.getAspect("auram"), 4).add(Aspect.getAspect("alienis"), 3).add(Aspect.getAspect("lucrum"), 3).add(Aspect.getAspect("praecantatio"), 5).add(Aspect.getAspect("ignis"), 8))                                                // Fire ess
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 9), AspectList().add(Aspect.getAspect("auram"), 4).add(Aspect.getAspect("alienis"), 3).add(Aspect.getAspect("lucrum"), 3).add(Aspect.getAspect("praecantatio"), 5).add(Aspect.getAspect("gelum"), 8))                                                // Ice ess
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 10), AspectList().add(Aspect.getAspect("auram"), 1).add(Aspect.getAspect("potentia"), 1).add(Aspect.getAspect("praecantatio"), 1))                                                                                                                                        // Iffesal dust
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 11), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("lucrum"), 6).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("terra"), 8))                                                                                            // Prim rune
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 12), AspectList().add(Aspect.getAspect("auram"), 3).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("terra"), 2).add(Aspect.getAspect("ignis"), 12))                                                                                            // Fire rune
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 13), AspectList().add(Aspect.getAspect("auram"), 3).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("terra"), 2).add(Aspect.getAspect("gelum"), 12))                                                                                            // Ice rune
		ThaumcraftApi.registerObjectTag(ItemStack(elvenResource, 1, 14), AspectList().add(Aspect.getAspect("arbor"), 1).add(Aspect.getAspect("praecantatio"), 2))                                                                                            // Twig
		ThaumcraftApi.registerObjectTag(ItemStack(elementalHelmet), AspectList().add(Aspect.getAspect("metallum"), 15).add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("tutamen"), 5).add(Aspect.getAspect("aqua"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elementalHelmetRevealing), AspectList().add(Aspect.getAspect("metallum"), 15).add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("tutamen"), 5).add(Aspect.getAspect("aqua"), 8).add(Aspect.getAspect("sensus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(elementalChestplate), AspectList().add(Aspect.getAspect("metallum"), 24).add(Aspect.getAspect("praecantatio"), 18).add(Aspect.getAspect("tutamen"), 8).add(Aspect.getAspect("terra"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elementalLeggings), AspectList().add(Aspect.getAspect("metallum"), 21).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("tutamen"), 7).add(Aspect.getAspect("ignis"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elementalBoots), AspectList().add(Aspect.getAspect("metallum"), 12).add(Aspect.getAspect("praecantatio"), 10).add(Aspect.getAspect("tutamen"), 4).add(Aspect.getAspect("aer"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumHelmet), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 35).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 15).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumHelmetRevealing), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 35).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 15).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumChestplate), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 56).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 24).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumLeggings), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 49).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 21).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))
		ThaumcraftApi.registerObjectTag(ItemStack(elvoriumBoots), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("metallum"), 28).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("tutamen"), 12).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("lucrum"), 30))
		ThaumcraftApi.registerObjectTag(ItemStack(realitySword), AspectList().add(Aspect.getAspect("auram"), 54).add(Aspect.getAspect("lucrum"), 49).add(Aspect.getAspect("metallum"), 18).add(Aspect.getAspect("potentia"), 36).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("telum"), 12))
		ThaumcraftApi.registerObjectTag(ItemStack(livingrockPickaxe), AspectList().add(Aspect.getAspect("arbor"), 1).add(Aspect.getAspect("perditio"), 2).add(Aspect.getAspect("perfodio"), 2).add(Aspect.getAspect("terra"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(manasteelHoe), AspectList().add(Aspect.getAspect("meto"), 4).add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("praecantatio"), 1))
		ThaumcraftApi.registerObjectTag(ItemStack(elementiumHoe), AspectList().add(Aspect.getAspect("meto"), 4).add(Aspect.getAspect("metallum"), 6).add(Aspect.getAspect("praecantatio"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(rodFire), AspectList().add(Aspect.getAspect("auram"), 32).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("potentia"), 24).add(Aspect.getAspect("instrumentum"), 8).add(Aspect.getAspect("metallum"), 8).add(Aspect.getAspect("ignis"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(rodIce), AspectList().add(Aspect.getAspect("auram"), 32).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("potentia"), 24).add(Aspect.getAspect("instrumentum"), 8).add(Aspect.getAspect("metallum"), 8).add(Aspect.getAspect("gelum"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(excaliber), AspectList().add(Aspect.getAspect("motus"), 16).add(Aspect.getAspect("potentia"), 16).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("telum"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(mask), AspectList().add(Aspect.getAspect("alienis"), 16).add(Aspect.getAspect("fames"), 16).add(Aspect.getAspect("sano"), 16).add(Aspect.getAspect("tenebrae"), 16).add(Aspect.getAspect("tutamen"), 16).add(Aspect.getAspect("vacuos"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(flugelSoul), AspectList().add(Aspect.getAspect("auram"), 16).add(Aspect.getAspect("alienis"), 16).add(Aspect.getAspect("iter"), 16).add(Aspect.getAspect("ordo"), 16).add(Aspect.getAspect("praecantatio"), 16).add(Aspect.getAspect("spiritus"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(flugelDisc), AspectList().add(Aspect.getAspect("aer"), 4).add(Aspect.getAspect("lucrum"), 4).add(Aspect.getAspect("sensus"), 4).add(Aspect.getAspect("lux"), 2).add(Aspect.getAspect("auram"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(elfFirePendant), AspectList().add(Aspect.getAspect("praecantatio"), 24).add(Aspect.getAspect("auram"), 8).add(Aspect.getAspect("tutamen"), 6).add(Aspect.getAspect("ignis"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(elfIcePendant), AspectList().add(Aspect.getAspect("praecantatio"), 24).add(Aspect.getAspect("auram"), 8).add(Aspect.getAspect("tutamen"), 6).add(Aspect.getAspect("gelum"), 8))
		ThaumcraftApi.registerObjectTag(ItemStack(pixieAttractor), AspectList().add(Aspect.getAspect("praecantatio"), 12).add(Aspect.getAspect("metallum"), 12).add(Aspect.getAspect("vitreus"), 4).add(Aspect.getAspect("fames"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(creativeReachPendant), AspectList().add(Aspect.getAspect("aer"), 64).add(Aspect.getAspect("aqua"), 64).add(Aspect.getAspect("ignis"), 64).add(Aspect.getAspect("ordo"), 64).add(Aspect.getAspect("perditio"), 64).add(Aspect.getAspect("terra"), 64))
		ThaumcraftApi.registerObjectTag(ItemStack(manaStone), AspectList().add(Aspect.getAspect("auram"), 4).add(Aspect.getAspect("potentia"), 4).add(Aspect.getAspect("praecantatio"), 10).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(manaStoneGreater), AspectList().add(Aspect.getAspect("auram"), 12).add(Aspect.getAspect("potentia"), 4).add(Aspect.getAspect("praecantatio"), 20).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(manaRingElven), AspectList().add(Aspect.getAspect("auram"), 4).add(Aspect.getAspect("metallum"), 12).add(Aspect.getAspect("praecantatio"), 30).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(manaRingGod), AspectList().add(Aspect.getAspect("auram"), 32).add(Aspect.getAspect("metallum"), 24).add(Aspect.getAspect("potentia"), 24).add(Aspect.getAspect("praecantatio"), 64).add(Aspect.getAspect("vitreus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(auraRingElven), AspectList().add(Aspect.getAspect("auram"), 4).add(Aspect.getAspect("lucrum"), 4).add(Aspect.getAspect("metallum"), 12).add(Aspect.getAspect("praecantatio"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(auraRingGod), AspectList().add(Aspect.getAspect("auram"), 16).add(Aspect.getAspect("alienis"), 4).add(Aspect.getAspect("lucrum"), 8).add(Aspect.getAspect("metallum"), 16).add(Aspect.getAspect("potentia"), 12).add(Aspect.getAspect("praecantatio"), 40))
		ThaumcraftApi.registerObjectTag(ItemStack(peacePipe), AspectList().add(Aspect.getAspect("spiritus"), 4).add(Aspect.getAspect("arbor"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(paperBreak), AspectList().add(Aspect.getAspect("spiritus"), 4).add(Aspect.getAspect("sensus"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(AlfheimItems.flugelHead), AspectList().add(Aspect.getAspect("alienis"), 8).add(Aspect.getAspect("humanus"), 4).add(Aspect.getAspect("mortuus"), 8).add(Aspect.getAspect("spiritus"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(astrolabe), AspectList().add(Aspect.getAspect("instrumentum"), 4).add(Aspect.getAspect("iter"), 2).add(Aspect.getAspect("metallum"), 8).add(Aspect.getAspect("praecantatio"), 6))
		ThaumcraftApi.registerObjectTag(ItemStack(invisibilityCloak), AspectList().add(Aspect.getAspect("pannus"), 6).add(Aspect.getAspect("fabrico"), 1).add(Aspect.getAspect("sensus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(dodgeRing), AspectList().add(Aspect.getAspect("vitreus"), 3).add(Aspect.getAspect("metallum"), 8).add(Aspect.getAspect("lucrum"), 3).add(Aspect.getAspect("motus"), 6))
		ThaumcraftApi.registerObjectTag(ItemStack(lootInterceptor), AspectList().add(Aspect.getAspect("praecantatio"), 5).add(Aspect.getAspect("metallum"), 4).add(Aspect.getAspect("auram"), 2).add(Aspect.getAspect("potentia"), 2).add(Aspect.getAspect("fames"), 16))
		ThaumcraftApi.registerObjectTag(ItemStack(rodGrass), AspectList().add(Aspect.getAspect("herba"), 7).add(Aspect.getAspect("terra"), 3).add(Aspect.getAspect("sano"), 2).add(Aspect.getAspect("praecantatio"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(cloudPendant), AspectList().add(Aspect.getAspect("tempestas"), 2).add(Aspect.getAspect("volatus"), 4).add(Aspect.getAspect("motus"), 4))
		ThaumcraftApi.registerObjectTag(ItemStack(cloudPendantSuper), AspectList().add(Aspect.getAspect("tempestas"), 2).add(Aspect.getAspect("volatus"), 6).add(Aspect.getAspect("motus"), 8))
		
		// For transmutation recipe
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 7), AspectList().add(Aspect.getAspect("metallum"), 3).add(Aspect.getAspect("praecantatio"), 2))
		ThaumcraftApi.registerObjectTag(ItemStack(manaResource, 1, 19), AspectList().add(Aspect.getAspect("metallum"), 1).add(Aspect.getAspect("praecantatio"), 1))
	}
}
