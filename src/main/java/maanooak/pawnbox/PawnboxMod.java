package maanooak.pawnbox;

import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.registries.RecipeTechRegistry;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;


@ModEntry
public class PawnboxMod {

    public void init() {
        ObjectRegistry.registerObject("pawnbox", new PawnboxInventoryObject(), 200, true);
    }

    public void postInit() {

        Recipes.registerModRecipe(new Recipe(
                "pawnbox",
                1,
                RecipeTechRegistry.DEMONIC,
                new Ingredient[] {
                        new Ingredient("storagebox", 1),
                        new Ingredient("coin", 5000)
                }));

    }

}
