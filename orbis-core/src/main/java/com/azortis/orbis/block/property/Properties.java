/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2021  Azortis
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.azortis.orbis.block.property;

@SuppressWarnings("unused")
public final class Properties {
    public static final BooleanProperty ATTACHED = BooleanProperty.create("attached");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty CONDITIONAL = BooleanProperty.create("conditional");
    public static final BooleanProperty DISARMED = BooleanProperty.create("disarmed");
    public static final BooleanProperty DRAG = BooleanProperty.create("drag");
    public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");
    public static final BooleanProperty EXTENDED = BooleanProperty.create("extended");
    public static final BooleanProperty EYE = BooleanProperty.create("eye");
    public static final BooleanProperty FALLING = BooleanProperty.create("falling");
    public static final BooleanProperty HANGING = BooleanProperty.create("hanging");
    public static final BooleanProperty HAS_BOTTLE_0 = BooleanProperty.create("has_bottle_0");
    public static final BooleanProperty HAS_BOTTLE_1 = BooleanProperty.create("has_bottle_1");
    public static final BooleanProperty HAS_BOTTLE_2 = BooleanProperty.create("has_bottle_2");
    public static final BooleanProperty HAS_RECORD = BooleanProperty.create("has_record");
    public static final BooleanProperty HAS_BOOK = BooleanProperty.create("has_book");
    public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");
    public static final BooleanProperty IN_WALL = BooleanProperty.create("in_wall");
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
    public static final BooleanProperty OCCUPIED = BooleanProperty.create("occupied");
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final BooleanProperty PERSISTENT = BooleanProperty.create("persistent");
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final BooleanProperty SHORT = BooleanProperty.create("short");
    public static final BooleanProperty SIGNAL_FIRE = BooleanProperty.create("signal_fire");
    public static final BooleanProperty SNOWY = BooleanProperty.create("snowy");
    public static final BooleanProperty TRIGGERED = BooleanProperty.create("triggered");
    public static final BooleanProperty UNSTABLE = BooleanProperty.create("unstable");
    public static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");
    public static final BooleanProperty VINE_END = BooleanProperty.create("vine_end");
    public static final BooleanProperty BERRIES = BooleanProperty.create("berries");
    public static final EnumProperty<Axis> HORIZONTAL_AXIS = EnumProperty.create("axis", Axis.class, Axis.X, Axis.Z);
    public static final EnumProperty<Axis> AXIS = EnumProperty.create("axis", Axis.class);
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);
    public static final EnumProperty<Direction> FACING_HOPPER = EnumProperty.create("facing", Direction.class, (facing) -> facing != Direction.UP);
    public static final EnumProperty<Direction> HORIZONTAL_FACING = EnumProperty.create("facing", Direction.class, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    // TODO add Jigsaw orientation enum with property
    public static final EnumProperty<AttachFace> ATTACH_FACE = EnumProperty.create("face", AttachFace.class);
    public static final EnumProperty<BellAttachType> BELL_ATTACHMENT = EnumProperty.create("attachment", BellAttachType.class);
    public static final EnumProperty<WallSide> NORTH_WALL = EnumProperty.create("north", WallSide.class);
    public static final EnumProperty<WallSide> EAST_WALL = EnumProperty.create("east", WallSide.class);
    public static final EnumProperty<WallSide> SOUTH_WALL = EnumProperty.create("south", WallSide.class);
    public static final EnumProperty<WallSide> WEST_WALL = EnumProperty.create("west", WallSide.class);
    public static final EnumProperty<RedstoneSide> NORTH_REDSTONE = EnumProperty.create("north", RedstoneSide.class);
    public static final EnumProperty<RedstoneSide> EAST_REDSTONE = EnumProperty.create("east", RedstoneSide.class);
    public static final EnumProperty<RedstoneSide> SOUTH_REDSTONE = EnumProperty.create("south", RedstoneSide.class);
    public static final EnumProperty<RedstoneSide> WEST_REDSTONE = EnumProperty.create("west", RedstoneSide.class);
    public static final EnumProperty<DoubleBlockHalf> DOUBLE_BLOCK_HALF = EnumProperty.create("half", DoubleBlockHalf.class);
    public static final EnumProperty<Half> HALF = EnumProperty.create("half", Half.class);
    public static final EnumProperty<RailShape> RAIL_SHAPE = EnumProperty.create("shape", RailShape.class);
    public static final EnumProperty<RailShape> RAIL_SHAPE_STRAIGHT = EnumProperty.create("shape", RailShape.class, (shape) -> shape != RailShape.NORTH_EAST && shape != RailShape.NORTH_WEST && shape != RailShape.SOUTH_EAST && shape != RailShape.SOUTH_WEST);
    public static final IntegerProperty AGE_1 = IntegerProperty.create("age", 0, 1);
    public static final IntegerProperty AGE_2 = IntegerProperty.create("age", 0, 2);
    public static final IntegerProperty AGE_3 = IntegerProperty.create("age", 0, 3);
    public static final IntegerProperty AGE_5 = IntegerProperty.create("age", 0, 5);
    public static final IntegerProperty AGE_7 = IntegerProperty.create("age", 0, 7);
    public static final IntegerProperty AGE_15 = IntegerProperty.create("age", 0, 15);
    public static final IntegerProperty AGE_25 = IntegerProperty.create("age", 0, 25);
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);
    public static final IntegerProperty CANDLES = IntegerProperty.create("candles", 1, 4);
    public static final IntegerProperty DELAY = IntegerProperty.create("delay", 1, 4);
    public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 1, 7);
    public static final IntegerProperty SCAFFOLDING_DISTANCE = IntegerProperty.create("distance", 0, 7);
    public static final IntegerProperty EGGS = IntegerProperty.create("eggs", 1, 4);
    public static final IntegerProperty HATCH = IntegerProperty.create("hatch", 0, 2);
    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 8);
    public static final IntegerProperty LEVEL_CAULDRON = IntegerProperty.create("level", 1, 3);
    public static final IntegerProperty LEVEL_COMPOSTER = IntegerProperty.create("level", 0, 8);
    public static final IntegerProperty LEVEL_FLOWING = IntegerProperty.create("level", 1, 8);
    public static final IntegerProperty LEVEL_HONEY = IntegerProperty.create("honey_level", 0, 5);
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 15);
    public static final IntegerProperty MOISTURE = IntegerProperty.create("moisture", 0, 7);
    public static final IntegerProperty NOTE = IntegerProperty.create("note", 0, 24);
    public static final IntegerProperty PICKLES = IntegerProperty.create("pickles", 1, 4);
    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 1);
    public static final IntegerProperty RESPAWN_ANCHOR_CHARGES = IntegerProperty.create("charges", 0, 4);
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 15); // TODO add Rotation enum for these values
    public static final EnumProperty<BedPart> BED_PART = EnumProperty.create("part", BedPart.class);
    public static final EnumProperty<ChestType> CHEST_TYPE = EnumProperty.create("type", ChestType.class);
    public static final EnumProperty<ComparatorMode> COMPARATOR_MODE = EnumProperty.create("mode", ComparatorMode.class);
    public static final EnumProperty<DoorHingeSide> DOOR_HINGE = EnumProperty.create("hinge", DoorHingeSide.class);
    public static final EnumProperty<NoteBlockInstrument> NOTE_BLOCK_INSTRUMENT = EnumProperty.create("instrument", NoteBlockInstrument.class);
    public static final EnumProperty<PistonType> PISTON_TYPE = EnumProperty.create("type", PistonType.class);
    public static final EnumProperty<SlabType> SLAB_TYPE = EnumProperty.create("type", SlabType.class);
    public static final EnumProperty<StairsShape> STAIRS_SHAPE = EnumProperty.create("shape", StairsShape.class);
    public static final EnumProperty<StructureMode> STRUCTURE_BLOCK_MODE = EnumProperty.create("mode", StructureMode.class);
    public static final EnumProperty<BambooLeaves> BAMBOO_LEAVES = EnumProperty.create("leaves", BambooLeaves.class);
    public static final EnumProperty<Tilt> TILT = EnumProperty.create("tilt", Tilt.class);
    public static final EnumProperty<Direction> VERTICAL_DIRECTION = EnumProperty.create("vertical_direction", Direction.class, Direction.UP, Direction.DOWN);
    public static final EnumProperty<DripstoneThickness> DRIPSTONE_THICKNESS = EnumProperty.create("thickness", DripstoneThickness.class);
    public static final EnumProperty<SculkSensorPhase> SCULK_SENSOR_PHASE = EnumProperty.create("sculk_sensor_phase", SculkSensorPhase.class);

}
