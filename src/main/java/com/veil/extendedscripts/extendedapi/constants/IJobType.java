package com.veil.extendedscripts.extendedapi.constants;

/**
 * This object stores npc job types available to all scripting handlers through the "Job" keyword.
 */
public interface IJobType {
    int NONE = 0;
    int BARD = 1;
    int HEALER = 2;
    int GUARD = 3;
    int ITEM_GIVER = 4;
    int FOLLOWER = 5;
    int SPAWNER = 6;
    int CONVERSATION = 7;
    int CHUNK_LOADER = 8;
}
