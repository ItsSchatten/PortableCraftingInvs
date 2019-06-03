package com.itsschatten.portablecrafting;

import com.itsschatten.libs.Permissions;
import com.itsschatten.portablecrafting.configs.Messages;

/**
 * A utility class that I can use to specify permissions, so I don't have to constantly rewrite them.
 */
public enum Perms implements Permissions {
    ANVIL {
        @Override
        public String getPermission() {
            return "pci.anvil";
        }

        @Override
        public String getNoPermission() {
            return Messages.NO_PERMS;
        }
    },

    ANVIL_OTHER {
        @Override
        public String getPermission() {
            return "pci.anvil.other";
        }

        @Override
        public String getNoPermission() {
            return Messages.NO_PERMS;
        }
    },

    CRAFTING {
        @Override
        public String getPermission() {
            return "pci.craft";
        }

        @Override
        public String getNoPermission() {
            return Messages.NO_PERMS;
        }
    },

    CRAFTING_OTHER {
        @Override
        public String getPermission() {
            return "pci.craft.other";
        }

        @Override
        public String getNoPermission() {
            return Messages.NO_PERMS;
        }
    },

    ENCHANTTABLE {
        @Override
        public String getPermission() {
            return "pci.enchanttable";
        }

        @Override
        public String getNoPermission() {
            return Messages.NO_PERMS;
        }
    },

    ENCHANTTABLE_OTHER {
        @Override
        public String getPermission() {
            return "pci.enchanttable.other";
        }

        @Override
        public String getNoPermission() {
            return Messages.NO_PERMS;
        }
    },

    ENDERCHEST {
        @Override
        public String getPermission() {
            return "pci.enderchest";
        }

        @Override
        public String getNoPermission() {
            return Messages.NO_PERMS;
        }
    },

    ENDERCHEST_OTHER {
        @Override
        public String getPermission() {
            return "pci.enderchest.other";
        }

        @Override
        public String getNoPermission() {
            return Messages.NO_PERMS;
        }
    },

    RELOAD {
        @Override
        public String getPermission() {
            return "pci.reload";
        }

        @Override
        public String getNoPermission() {
            return Messages.NO_PERMS;
        }
    },

    UPDATE_NOTIFICATIONS {
        @Override
        public String getPermission() {
            return "pci.update";
        }

        @Override
        public String getNoPermission() {
            return Messages.NO_PERMS;
        }
    }


}
