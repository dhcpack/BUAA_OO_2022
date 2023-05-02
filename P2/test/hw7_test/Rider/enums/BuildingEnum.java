package src.enums;

import static src.enums.DirectionEnum.DOWN;
import static src.enums.DirectionEnum.UP;

public enum BuildingEnum {
    A, B, C, D, E;

    public static boolean needSwitchDir(BuildingEnum start,
                                        BuildingEnum dest, DirectionEnum dir, int access) {
        DirectionEnum negDir = dir == UP ? DOWN : UP;
        int dirDistance = 0;
        BuildingEnum b = start;
        while (b != dest) {
            b = getNextBuilding(b, dir, access);
            dirDistance++;
        }
        b = start;
        int negDirDistance = 0;
        while (b != dest) {
            b = getNextBuilding(b, negDir, access);
            negDirDistance++;
        }
        return negDirDistance < dirDistance;
    }

    public static BuildingEnum find(int i) {
        for (BuildingEnum b : BuildingEnum.values()) {
            if (b.ordinal() == i) {
                return b;
            }
        }
        return null;
    }

    public static BuildingEnum getNextBuilding(BuildingEnum b, DirectionEnum dir, int access) {
        BuildingEnum next = b;
        if (dir == UP) {
            next = next == E ? A : find(next.ordinal() + 1);
            while (((1 << next.ordinal()) & access) == 0) {
                next = next == E ? A : find(next.ordinal() + 1);
            }
            return next;
        } else {
            next = next == A ? E : find(next.ordinal() - 1);
            while (((1 << next.ordinal()) & access) == 0) {
                next = next == A ? E : find(next.ordinal() - 1);
            }
            return next;
        }
    }

    public static BuildingEnum next(BuildingEnum b, DirectionEnum dir) {
        if (dir == UP) {
            return b == E ? A : find(b.ordinal() + 1);
        } else {
            return b == A ? E : find(b.ordinal() - 1);
        }
    }
}

