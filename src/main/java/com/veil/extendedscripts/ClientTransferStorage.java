package com.veil.extendedscripts;

/**
 * As it was explained to me, each player will have their own version of this class so despite the fact that
 * this is static, if accessed from the client side, the value can be different from player to player.
 * This class is used for caching values that are not easily transferred to other classes.
 */
public class ClientTransferStorage {
    public static float attackReach = 3F;
}
