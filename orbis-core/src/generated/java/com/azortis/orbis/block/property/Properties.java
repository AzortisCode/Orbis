package com.azortis.orbis.block.property;

import java.lang.SuppressWarnings;
import org.jetbrains.annotations.NotNull;

/**
 * This class has been autogenerated.
 */
@SuppressWarnings("unused")
public final class Properties {
  @NotNull
  public static final BooleanProperty ATTACHED = BooleanProperty.create("attached");

  @NotNull
  public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");

  @NotNull
  public static final BooleanProperty CONDITIONAL = BooleanProperty.create("conditional");

  @NotNull
  public static final BooleanProperty DISARMED = BooleanProperty.create("disarmed");

  @NotNull
  public static final BooleanProperty DRAG = BooleanProperty.create("drag");

  @NotNull
  public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");

  @NotNull
  public static final BooleanProperty EXTENDED = BooleanProperty.create("extended");

  @NotNull
  public static final BooleanProperty EYE = BooleanProperty.create("eye");

  @NotNull
  public static final BooleanProperty FALLING = BooleanProperty.create("falling");

  @NotNull
  public static final BooleanProperty HANGING = BooleanProperty.create("hanging");

  @NotNull
  public static final BooleanProperty HAS_BOTTLE_0 = BooleanProperty.create("has_bottle_0");

  @NotNull
  public static final BooleanProperty HAS_BOTTLE_1 = BooleanProperty.create("has_bottle_1");

  @NotNull
  public static final BooleanProperty HAS_BOTTLE_2 = BooleanProperty.create("has_bottle_2");

  @NotNull
  public static final BooleanProperty HAS_RECORD = BooleanProperty.create("has_record");

  @NotNull
  public static final BooleanProperty HAS_BOOK = BooleanProperty.create("has_book");

  @NotNull
  public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");

  @NotNull
  public static final BooleanProperty IN_WALL = BooleanProperty.create("in_wall");

  @NotNull
  public static final BooleanProperty LIT = BooleanProperty.create("lit");

  @NotNull
  public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

  @NotNull
  public static final BooleanProperty OCCUPIED = BooleanProperty.create("occupied");

  @NotNull
  public static final BooleanProperty OPEN = BooleanProperty.create("open");

  @NotNull
  public static final BooleanProperty PERSISTENT = BooleanProperty.create("persistent");

  @NotNull
  public static final BooleanProperty POWERED = BooleanProperty.create("powered");

  @NotNull
  public static final BooleanProperty SHORT = BooleanProperty.create("short");

  @NotNull
  public static final BooleanProperty SIGNAL_FIRE = BooleanProperty.create("signal_fire");

  @NotNull
  public static final BooleanProperty SNOWY = BooleanProperty.create("snowy");

  @NotNull
  public static final BooleanProperty TRIGGERED = BooleanProperty.create("triggered");

  @NotNull
  public static final BooleanProperty UNSTABLE = BooleanProperty.create("unstable");

  @NotNull
  public static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");

  @NotNull
  public static final BooleanProperty VINE_END = BooleanProperty.create("vine_end");

  @NotNull
  public static final BooleanProperty BERRIES = BooleanProperty.create("berries");

  @NotNull
  public static final EnumProperty<Axis> HORIZONTAL_AXIS = EnumProperty.create("axis", Axis.class, (axis) -> axis != Axis.Y);

  @NotNull
  public static final EnumProperty<Axis> AXIS = EnumProperty.create("axis", Axis.class);

  @NotNull
  public static final BooleanProperty UP = BooleanProperty.create("up");

  @NotNull
  public static final BooleanProperty DOWN = BooleanProperty.create("down");

  @NotNull
  public static final BooleanProperty NORTH = BooleanProperty.create("north");

  @NotNull
  public static final BooleanProperty EAST = BooleanProperty.create("east");

  @NotNull
  public static final BooleanProperty SOUTH = BooleanProperty.create("south");

  @NotNull
  public static final BooleanProperty WEST = BooleanProperty.create("west");

  @NotNull
  public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);

  @NotNull
  public static final EnumProperty<Direction> FACING_HOPPER = EnumProperty.create("facing", Direction.class, (facing) -> facing != Direction.UP);

  @NotNull
  public static final EnumProperty<Direction> HORIZONTAL_FACING = EnumProperty.create("facing", Direction.class, (facing) -> facing != Direction.DOWN && facing != Direction.UP);

  @NotNull
  public static final EnumProperty<Orientation> ORIENTATION = EnumProperty.create("orientation", Orientation.class);

  @NotNull
  public static final EnumProperty<AttachFace> ATTACH_FACE = EnumProperty.create("face", AttachFace.class);

  @NotNull
  public static final EnumProperty<BellAttachType> BELL_ATTACHMENT = EnumProperty.create("attachment", BellAttachType.class);

  @NotNull
  public static final EnumProperty<WallSide> EAST_WALL = EnumProperty.create("east", WallSide.class);

  @NotNull
  public static final EnumProperty<WallSide> NORTH_WALL = EnumProperty.create("north", WallSide.class);

  @NotNull
  public static final EnumProperty<WallSide> SOUTH_WALL = EnumProperty.create("south", WallSide.class);

  @NotNull
  public static final EnumProperty<WallSide> WEST_WALL = EnumProperty.create("west", WallSide.class);

  @NotNull
  public static final EnumProperty<RedstoneSide> EAST_REDSTONE = EnumProperty.create("east", RedstoneSide.class);

  @NotNull
  public static final EnumProperty<RedstoneSide> NORTH_REDSTONE = EnumProperty.create("north", RedstoneSide.class);

  @NotNull
  public static final EnumProperty<RedstoneSide> SOUTH_REDSTONE = EnumProperty.create("south", RedstoneSide.class);

  @NotNull
  public static final EnumProperty<RedstoneSide> WEST_REDSTONE = EnumProperty.create("west", RedstoneSide.class);

  @NotNull
  public static final EnumProperty<DoubleBlockHalf> DOUBLE_BLOCK_HALF = EnumProperty.create("half", DoubleBlockHalf.class);

  @NotNull
  public static final EnumProperty<Half> HALF = EnumProperty.create("half", Half.class);

  @NotNull
  public static final EnumProperty<RailShape> RAIL_SHAPE = EnumProperty.create("shape", RailShape.class);

  @NotNull
  public static final EnumProperty<RailShape> RAIL_SHAPE_STRAIGHT = EnumProperty.create("shape", RailShape.class, (shape) -> shape != RailShape.SOUTH_EAST && shape != RailShape.SOUTH_WEST && shape != RailShape.NORTH_WEST && shape != RailShape.NORTH_EAST);

  @NotNull
  public static final IntegerProperty AGE_1 = IntegerProperty.create("age", 0, 1);

  @NotNull
  public static final IntegerProperty AGE_2 = IntegerProperty.create("age", 0, 2);

  @NotNull
  public static final IntegerProperty AGE_3 = IntegerProperty.create("age", 0, 3);

  @NotNull
  public static final IntegerProperty AGE_5 = IntegerProperty.create("age", 0, 5);

  @NotNull
  public static final IntegerProperty AGE_7 = IntegerProperty.create("age", 0, 7);

  @NotNull
  public static final IntegerProperty AGE_15 = IntegerProperty.create("age", 0, 15);

  @NotNull
  public static final IntegerProperty AGE_25 = IntegerProperty.create("age", 0, 25);

  @NotNull
  public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);

  @NotNull
  public static final IntegerProperty CANDLES = IntegerProperty.create("candles", 1, 4);

  @NotNull
  public static final IntegerProperty DELAY = IntegerProperty.create("delay", 1, 4);

  @NotNull
  public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 1, 7);

  @NotNull
  public static final IntegerProperty EGGS = IntegerProperty.create("eggs", 1, 4);

  @NotNull
  public static final IntegerProperty HATCH = IntegerProperty.create("hatch", 0, 2);

  @NotNull
  public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 8);

  @NotNull
  public static final IntegerProperty LEVEL_CAULDRON = IntegerProperty.create("level", 1, 3);

  @NotNull
  public static final IntegerProperty LEVEL_COMPOSTER = IntegerProperty.create("level", 0, 8);

  @NotNull
  public static final IntegerProperty LEVEL_FLOWING = IntegerProperty.create("level", 1, 8);

  @NotNull
  public static final IntegerProperty LEVEL_HONEY = IntegerProperty.create("honey_level", 0, 5);

  @NotNull
  public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 15);

  @NotNull
  public static final IntegerProperty MOISTURE = IntegerProperty.create("moisture", 0, 7);

  @NotNull
  public static final IntegerProperty NOTE = IntegerProperty.create("note", 0, 24);

  @NotNull
  public static final IntegerProperty PICKLES = IntegerProperty.create("pickles", 1, 4);

  @NotNull
  public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

  @NotNull
  public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 1);

  @NotNull
  public static final IntegerProperty STABILITY_DISTANCE = IntegerProperty.create("distance", 0, 7);

  @NotNull
  public static final IntegerProperty RESPAWN_ANCHOR_CHARGES = IntegerProperty.create("charges", 0, 4);

  @NotNull
  public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 15);

  @NotNull
  public static final EnumProperty<BedPart> BED_PART = EnumProperty.create("part", BedPart.class);

  @NotNull
  public static final EnumProperty<ChestType> CHEST_TYPE = EnumProperty.create("type", ChestType.class);

  @NotNull
  public static final EnumProperty<ComparatorMode> COMPARATOR_MODE = EnumProperty.create("mode", ComparatorMode.class);

  @NotNull
  public static final EnumProperty<DoorHingeSide> DOOR_HINGE = EnumProperty.create("hinge", DoorHingeSide.class);

  @NotNull
  public static final EnumProperty<NoteBlockInstrument> NOTE_BLOCK_INSTRUMENT = EnumProperty.create("instrument", NoteBlockInstrument.class);

  @NotNull
  public static final EnumProperty<PistonType> PISTON_TYPE = EnumProperty.create("type", PistonType.class);

  @NotNull
  public static final EnumProperty<SlabType> SLAB_TYPE = EnumProperty.create("type", SlabType.class);

  @NotNull
  public static final EnumProperty<StairsShape> STAIRS_SHAPE = EnumProperty.create("shape", StairsShape.class);

  @NotNull
  public static final EnumProperty<StructureMode> STRUCTURE_BLOCK_MODE = EnumProperty.create("mode", StructureMode.class);

  @NotNull
  public static final EnumProperty<BambooLeaves> BAMBOO_LEAVES = EnumProperty.create("leaves", BambooLeaves.class);

  @NotNull
  public static final EnumProperty<Tilt> TILT = EnumProperty.create("tilt", Tilt.class);

  @NotNull
  public static final EnumProperty<Direction> VERTICAL_DIRECTION = EnumProperty.create("vertical_direction", Direction.class, Direction.UP, Direction.DOWN);

  @NotNull
  public static final EnumProperty<DripstoneThickness> DRIPSTONE_THICKNESS = EnumProperty.create("thickness", DripstoneThickness.class);

  @NotNull
  public static final EnumProperty<SculkSensorPhase> SCULK_SENSOR_PHASE = EnumProperty.create("sculk_sensor_phase", SculkSensorPhase.class);
}