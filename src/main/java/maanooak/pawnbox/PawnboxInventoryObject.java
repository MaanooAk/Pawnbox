package maanooak.pawnbox;

import java.awt.Color;

import necesse.engine.localization.Localization;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.InventoryObjectEntity;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.level.gameObject.furniture.StorageBoxInventoryObject;
import necesse.level.maps.Level;


public class PawnboxInventoryObject extends StorageBoxInventoryObject {

    public PawnboxInventoryObject() {
        super("pawnbox", 40, new Color(83, 67, 119));
    }

    @Override
    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        final ListGameTooltips tooltips = super.getItemTooltips(item, perspective);
        tooltips.add(Localization.translate("itemtooltip", "pawnboxtip"));
        return tooltips;
    }

    @Override
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new PawnInventoryObjectEntity(level, x, y, this.slots);
    }

    public static class PawnInventoryObjectEntity extends InventoryObjectEntity {

        private static final int pawnInterval = 1000;

        private long nextPawnTime;

        public PawnInventoryObjectEntity(Level level, int x, int y, int slots) {
            super(level, x, y, slots);
            nextPawnTime = 0;
        }

        @Override
        public void addSaveData(SaveData save) {
            super.addSaveData(save);
            save.addLong("nextPawnTime", this.nextPawnTime);
        }

        @Override
        public void applyLoadData(LoadData save) {
            super.applyLoadData(save);
            this.nextPawnTime = save.getLong("nextPawnTime", 0L);
        }

        private void tick() {
            final long currentTime = this.getWorldEntity().getWorldTime();

            while (currentTime >= nextPawnTime) {

                final InventoryItem inventoryItem = getItemToPawn();
                if (inventoryItem == null) {
                    nextPawnTime = currentTime + pawnInterval;
                    break;
                }

                final float brokerValue = inventoryItem.item.getBrokerValue(inventoryItem);
                final int amount = (brokerValue >= 1) ? 1 : (int) Math.ceil(1 / brokerValue);

                final InventoryItem coins = new InventoryItem("coin", (int) (amount * brokerValue));
                if (inventory.canAddItem(getLevel(), null, coins, "pawn") == coins.getAmount()) {
                    inventory.addItem(getLevel(), null, coins, "pawn");

                    inventory.removeItems(getLevel(), null, inventoryItem.item, amount, "pawn");
                }

                nextPawnTime += pawnInterval;
            }

        }

        private InventoryItem getItemToPawn() {

            final int size = inventory.getSize();
            for (int slot = 0; slot < size; slot++) {

                final int amount = inventory.getAmount(slot);
                if (amount == 0) continue;

                final InventoryItem inventoryItem = inventory.getItem(slot);
                if (inventoryItem == null || inventoryItem.item == null) continue;
                if (inventoryItem.item.getStringID().equals("coin")) continue;
                if (inventoryItem.item.getStringID().equals("coinpouch")) continue;

                final float brokerValue = inventoryItem.item.getBrokerValue(inventoryItem);
                if (brokerValue * amount < 1) continue;

                return inventoryItem;
            }
            return null;
        }

        @Override
        public void serverTick() {
            tick();
            super.serverTick();
        }

    }

}
