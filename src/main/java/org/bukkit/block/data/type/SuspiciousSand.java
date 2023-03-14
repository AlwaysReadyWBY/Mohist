package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * 'dusted' represents how far uncovered by brush the block is.
 */
public interface SuspiciousSand extends BlockData {

    /**
     * Gets the value of the 'dusted' property.
     *
     * @return the 'dusted' value
     */
    int getDusted();

    /**
     * Sets the value of the 'dusted' property.
     *
     * @param dusted the new 'dusted' value
     */
    void setDusted(int dusted);

    /**
     * Gets the maximum allowed value of the 'dusted' property.
     *
     * @return the maximum 'dusted' value
     */
    int getMaximumDusted();
}
