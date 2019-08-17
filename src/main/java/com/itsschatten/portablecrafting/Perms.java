package com.itsschatten.portablecrafting;

import com.itsschatten.libs.interfaces.IPermissions;
import com.itsschatten.portablecrafting.configs.Messages;

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
