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
