package com.rengv.pokecubeutils.utils;

import net.minecraft.nbt.NbtList;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventManager {
    public static boolean STARTED = false;
    public static boolean CAN_JOIN_EVENT = false;
    public static boolean CAN_RIDE = false;
    public static boolean CAN_USE_POKEMON = false;
    public static boolean PVP = false;
    public static boolean PVE = false;
//    public static boolean CAN_FLY = false;
    public static boolean INFINITE_RIDE_STAMINE = false;
    public static boolean CAN_BUILD = false;
    public static boolean CAN_BREAK = false;

    public static final Set<UUID> leaveBypass = new HashSet<>();
    public static final Set<UUID> enterBypass = new HashSet<>();
    public static final Set<UUID> playerFrozen = new HashSet<>();

    public static NbtList kitItems = null;
}
