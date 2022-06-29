package maanooak.pawnbox;

import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.registries.RecipeTechRegistry;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;


@ModEntry
public class PawnboxMod {

    public void init() {

        ObjectRegistry.registerObject("pawnbox", new PawnboxInventoryObject(), 200, true);

//        PacketRegistry.registerPacket(ExamplePacket.class);
    }

    public void initResources() {
//        ExampleMob.texture = GameTexture.fromFile("mobs/examplemob");
    }

    public void postInit() {

        final int coinCost = ItemRegistry.getItem("coin").getStackSize();
        Recipes.registerModRecipe(new Recipe(
                "pawnbox",
                1,
                RecipeTechRegistry.DEMONIC,
                new Ingredient[] {
                        new Ingredient("storagebox", 1),
                        new Ingredient("coin", coinCost)
                }));

    }

}
