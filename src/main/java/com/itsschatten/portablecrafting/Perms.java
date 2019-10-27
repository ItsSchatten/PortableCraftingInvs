package com.itsschatten.portablecrafting;

import com.itsschatten.libs.interfaces.IPermissions;

/**
 * A utility class that I can use to specify permissions, so I don't have to constantly rewrite them.
 */
public enum Perms implements IPermissions {
    ANVIL {
        @Override
        public String getPermission() {
            return "pci.anvil";
        }
    },

    ANVIL_OTHER {
        @Override
        public String getPermission() {
            return "pci.anvil.other";
        }
    },

    CRAFTING {
        @Override
        public String getPermission() {
            return "pci.craft";
        }
    },

    CRAFTING_OTHER {
        @Override
        public String getPermission() {
            return "pci.craft.other";
        }
    },

    ENCHANTTABLE {
        @Override
        public String getPermission() {
            return "pci.enchanttable";
        }
    },

    ENCHANTTABLE_OTHER {
        @Override
        public String getPermission() {
            return "pci.enchanttable.other";
        }
    },

    ENDERCHEST {
        @Override
        public String getPermission() {
            return "pci.enderchest";
        }
    },

    ENDERCHEST_OTHER {
        @Override
        public String getPermission() {
            return "pci.enderchest.other";
        }
    },

    GRINDSTONE {
        @Override
        public String getPermission() {
            return "pci.grindstone";
        }
    },

    GRINDSTONE_OTHER {
        @Override
        public String getPermission() {
            return "pci.grindstone.other";
        }
    },

    LOOM {
        @Override
        public String getPermission() {
            return "pci.loom";
        }
    },

    LOOM_OTHER {
        @Override
        public String getPermission() {
            return "pci.loom.other";
        }
    },

    CARTOGRAPHY {
        @Override
        public String getPermission() {
            return "pci.cartography";
        }
    },

    CARTOGRAPHY_OTHER {
        @Override
        public String getPermission() {
            return "pci.cartography.other";
        }
    },

    STONECUTTER {
        @Override
        public String getPermission() {
            return "pci.stonecutter";
        }
    },

    STONECUTTER_OTHER {
        @Override
        public String getPermission() {
            return "pci.stonecutter.other";
        }
    },

    SIGN_CREATE {
        @Override
        public String getPermission() {
            return "pci.sign.create";
        }
    },

    SIGN_CREATE_ANVIL {
        @Override
        public String getPermission() {
            return "pci.sign.anvil";
        }
    },

    SIGN_CREATE_CATROGRAPHY {
        @Override
        public String getPermission() {
            return "pci.sign.cartography";
        }
    },

    SIGN_CREATE_CRAFTING {
        @Override
        public String getPermission() {
            return "pci.sign.crafting-table";
        }
    },

    SIGN_CREATE_ENCHANTTABLE {
        @Override
        public String getPermission() {
            return "pci.sign.enchanttable";
        }
    },

    SIGN_CREATE_ENDERCHEST {
        @Override
        public String getPermission() {
            return "pci.sign.enderchest";
        }
    },

    SIGN_CREATE_GRINDSTONE {
        @Override
        public String getPermission() {
            return "pci.sign.grindstone";
        }
    },

    SIGN_CREATE_LOOM {
        @Override
        public String getPermission() {
            return "pci.sign.loom";
        }
    },

    SIGN_CREATE_STONECUTTER {
        @Override
        public String getPermission() {
            return "pci.sign.stonecutter";
        }
    },

    USE_SIGN_ANVIL {
        @Override
        public String getPermission() {
            return "pci.sign.anvil.use";
        }
    },

    USE_SIGN_CATROGRAPHY {
        @Override
        public String getPermission() {
            return "pci.sign.cartography.use";
        }
    },

    USE_SIGN_CRAFTING {
        @Override
        public String getPermission() {
            return "pci.sign.crafting-table.use";
        }
    },

    USE_SIGN_ENCHANTTABLE {
        @Override
        public String getPermission() {
            return "pci.sign.enchanttable.use";
        }
    },

    USE_SIGN_ENDERCHEST {
        @Override
        public String getPermission() {
            return "pci.sign.enderchest.use";
        }
    },

    USE_SIGN_GRINDSTONE {
        @Override
        public String getPermission() {
            return "pci.sign.grindstone.use";
        }
    },

    USE_SIGN_LOOM {
        @Override
        public String getPermission() {
            return "pci.sign.loom.use";
        }
    },

    USE_SIGN_STONECUTTER {
        @Override
        public String getPermission() {
            return "pci.sign.stonecutter.use";
        }
    },

    RELOAD {
        @Override
        public String getPermission() {
            return "pci.reload";
        }
    },

    UPDATE_NOTIFICATIONS {
        @Override
        public String getPermission() {
            return "pci.update";
        }
    }


}
