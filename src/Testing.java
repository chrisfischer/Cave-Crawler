import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class Testing {

	@Test
	public void mobDamagesSquare() {

		ObjController obj = new ObjController();
		Square square = new Square(new TileMap());
		Mob flyer = new Flyer(0, 0, square.pos_x, square.pos_y, square);

		obj.addGameObj(square);
		obj.addGameObj(flyer);

		obj.tick();

		assertTrue(flyer.intersects(square));
		assertEquals(Square.MAX_HEALTH - Flyer.POWER, square.getCurrHealth());
	}
	
	@Test
	public void mobDamagesSquareTwice() {

		ObjController obj = new ObjController();
		Square square = new Square(new TileMap());
		Mob flyer = new Flyer(0, 0, square.pos_x, square.pos_y, square);

		obj.addGameObj(square);
		obj.addGameObj(flyer);
		
		assertTrue(flyer.intersects(square));
		
		obj.tick();
		assertTrue(flyer.intersects(square));
		assertEquals(Square.MAX_HEALTH - Flyer.POWER, square.getCurrHealth());
		
		obj.tick();
		assertEquals(Square.MAX_HEALTH - Flyer.POWER, square.getCurrHealth()); // health should not change
	}

	@Test
	public void multipleMobsDamagesSquare() {

		ObjController obj = new ObjController();
		Square square = new Square(new TileMap());
		Mob flyer1 = new Flyer(0, 0, square.pos_x, square.pos_y, square);
		Mob flyer2 = new Flyer(0, 0, square.pos_x, square.pos_y, square);

		obj.addGameObj(square);
		obj.addGameObj(flyer1);
		obj.addGameObj(flyer2);

		obj.tick();

		assertTrue(flyer1.intersects(square));
		assertTrue(flyer2.intersects(square));
		assertEquals(Square.MAX_HEALTH - Flyer.POWER * 2, square.getCurrHealth());
	}

	@Test
	public void mobIntersectsBullet() {
		ObjController obj = new ObjController();
		Square square = new Square(new TileMap());
		MobSpawner spawn = new MobSpawner(square, null, "CUSTOM");
		Mob flyer = new Flyer(0, 0, square.pos_x + Square.SIZE + 1, square.pos_y, square);
		Bullet bullet = new Bullet(flyer.pos_x, flyer.pos_y, Flyer.MAX_HEALTH - 1, Direction.RIGHT, new TileMap(),
				spawn);

		obj.addGameObj(bullet);
		obj.addGameObj(spawn);
		obj.addGameObj(square);
		spawn.addMob(flyer);

		obj.tick();

		assertFalse(flyer.intersects(square));
		assertTrue(flyer.intersects(bullet));
		assertEquals(Flyer.MAX_HEALTH - bullet.getPower(), flyer.getCurrHealth());

	}

	@Test
	public void mobIntersectsBulletHealthToZero() {
		ObjController obj = new ObjController();
		Square square = new Square(new TileMap());
		MobSpawner spawn = new MobSpawner(square, null, "CUSTOM");
		Mob flyer = new Flyer(0, 0, square.pos_x + Square.SIZE + 1, square.pos_y, square);
		Bullet bullet = new Bullet(flyer.pos_x, flyer.pos_y, Flyer.MAX_HEALTH, Direction.RIGHT, new TileMap(), spawn);

		obj.addGameObj(bullet);
		obj.addGameObj(spawn);
		obj.addGameObj(square);
		
		spawn.addMob(flyer);

		obj.tick();

		assertFalse(flyer.intersects(square));
		assertTrue(flyer.intersects(bullet));
		assertEquals(0, flyer.getCurrHealth());

		assertEquals(0, bullet.v_x);
		assertEquals(0, bullet.v_y);
		assertTrue(spawn.isMobListEmpty());
		assertEquals(Flyer.POINTS_WORTH, square.getScore());

	}
	
	// Doesn't matter that the health goes negative, as long as the effect is the same as to zero
	@Test
	public void mobIntersectsBulletHealthToNegtive() {
		ObjController obj = new ObjController();
		Square square = new Square(new TileMap());
		MobSpawner spawn = new MobSpawner(square, null, "CUSTOM");
		Mob flyer = new Flyer(0, 0, square.pos_x + Square.SIZE + 1, square.pos_y, square);
		Bullet bullet = new Bullet(flyer.pos_x, flyer.pos_y, Flyer.MAX_HEALTH + 100, Direction.RIGHT, new TileMap(), spawn);

		obj.addGameObj(bullet);
		obj.addGameObj(spawn);
		obj.addGameObj(square);
		spawn.addMob(flyer);

		obj.tick();

		assertFalse(flyer.intersects(square));
		assertTrue(flyer.intersects(bullet));
		assertEquals(-100, flyer.getCurrHealth());

		assertEquals(0, bullet.v_x);
		assertEquals(0, bullet.v_y);
		assertTrue(spawn.isMobListEmpty());
		assertEquals(Flyer.POINTS_WORTH, square.getScore());

	}

	@Test
	public void multipleMobsIntersectBullet() {
		ObjController obj = new ObjController();
		Square square = new Square(new TileMap());
		MobSpawner spawn = new MobSpawner(square, null, "CUSTOM");
		Mob flyer1 = new Flyer(0, 0, square.pos_x + Square.SIZE + 1, square.pos_y, square);
		Mob flyer2 = new Flyer(0, 0, square.pos_x + Square.SIZE + 1, square.pos_y, square);
		Bullet bullet = new Bullet(flyer1.pos_x, flyer1.pos_y, Flyer.MAX_HEALTH - 1, Direction.RIGHT, new TileMap(),
				spawn);

		obj.addGameObj(bullet);
		obj.addGameObj(spawn);
		obj.addGameObj(square);
		spawn.addMob(flyer1);
		spawn.addMob(flyer2);

		obj.tick();

		assertFalse(flyer1.intersects(square));
		assertTrue(flyer1.intersects(bullet));
		assertTrue(flyer2.intersects(bullet));
		assertEquals(Flyer.MAX_HEALTH - bullet.getPower(), flyer1.getCurrHealth()); // only damages the first one
		assertEquals(Flyer.MAX_HEALTH, flyer2.getCurrHealth());

	}
	
	// If two bullets intersect mob at the same time, they should both deal damage
	@Test
	public void mobIntersectsMultipleBullet() {
		ObjController obj = new ObjController();
		Square square = new Square(new TileMap());
		MobSpawner spawn = new MobSpawner(square, null, "CUSTOM");
		Mob flyer = new Flyer(0, 0, square.pos_x + Square.SIZE + 1, square.pos_y, square);
		Bullet bullet1 = new Bullet(flyer.pos_x, flyer.pos_y, Flyer.MAX_HEALTH, Direction.RIGHT, new TileMap(),
				spawn);
		Bullet bullet2 = new Bullet(flyer.pos_x, flyer.pos_y, Flyer.MAX_HEALTH, Direction.RIGHT, new TileMap(),
				spawn);

		obj.addGameObj(bullet1);
		obj.addGameObj(bullet2);
		obj.addGameObj(spawn);
		obj.addGameObj(square);
		spawn.addMob(flyer);

		obj.tick();

		assertFalse(flyer.intersects(square));
		assertTrue(flyer.intersects(bullet1));
		assertTrue(flyer.intersects(bullet2));
		assertEquals(Flyer.MAX_HEALTH - bullet1.getPower() - bullet2.getPower(), flyer.getCurrHealth());

	}
	
	@Test
	public void mobDiesWhileIntersectingSquare() {
		ObjController obj = new ObjController();
		Square square = new Square(new TileMap());
		MobSpawner spawn = new MobSpawner(square, null, "CUSTOM");
		Mob flyer = new Flyer(0, 0, square.pos_x, square.pos_y, square);
		Bullet bullet = new Bullet(flyer.pos_x, flyer.pos_y, Flyer.MAX_HEALTH, Direction.RIGHT, new TileMap(),
				spawn);

		obj.addGameObj(bullet);
		obj.addGameObj(spawn);
		obj.addGameObj(square);
		spawn.addMob(flyer);

		obj.tick();

		assertTrue(flyer.intersects(square));
		assertTrue(flyer.intersects(bullet));
		assertTrue(square.intersects(bullet));
		assertEquals(0, flyer.getCurrHealth());
		assertTrue(spawn.isMobListEmpty());
		assertEquals(Square.MAX_HEALTH, square.getCurrHealth());

	}
	
	@Test
	public void flyerMoves() {
		ObjController obj = new ObjController();
		Square square = new Square(new TileMap());
		MobSpawner spawn = new MobSpawner(square, null, "CUSTOM");
		Mob flyer = new Flyer(30, 0, square.pos_x, square.pos_y, square);
		
		obj.addGameObj(spawn);
		obj.addGameObj(square);
		spawn.addMob(flyer);
		
		assertTrue(flyer.intersects(square));
		
		obj.tick();

		assertFalse(flyer.intersects(square));
		assertEquals(30, flyer.pos_x);

	}
	
	@Test
	public void walkerMoves() {
		ObjController obj = new ObjController();
		TileMap map = new TileMap();
		Square square = new Square(map);
		MobSpawner spawn = new MobSpawner(square, map, "CUSTOM");
		Mob walker = new Walker(30, 0, square.pos_x, square.pos_y, map, square);
		
		obj.addGameObj(spawn);
		obj.addGameObj(square);
		spawn.addMob(walker);
		
		assertTrue(walker.intersects(square));
		
		obj.tick();

		assertFalse(walker.intersects(square));
		assertEquals(30, walker.pos_x);

	}
	
	@Test
	public void mobSpawnsFlyer() {
		ObjController obj = new ObjController();
		FileManager files = new FileManager(); // needed to prevent null pointer exception
		TileMap map = new TileMap();
		Square square = new Square(map);
		MobSpawner spawn = new MobSpawner(square, null, "CUSTOM");
		List<Mob> flyers = Flyer.spawn(10, map, square);
		
		obj.addGameObj(spawn);
		obj.addGameObj(square);
		for (Mob m : flyers) {
			spawn.addMob(m);
		}
		
		obj.tick();

		assertEquals(10, spawn.getMobs().size());
		
	}
	
	@Test
	public void mobSpawnsWalker() {
		ObjController obj = new ObjController();
		FileManager files = new FileManager(); // needed to prevent null pointer exception
		TileMap map = new TileMap();
		Square square = new Square(map);
		MobSpawner spawn = new MobSpawner(square, null, "CUSTOM");
		List<Mob> flyers = Walker.spawn(10, map, square);
		
		obj.addGameObj(spawn);
		obj.addGameObj(square);
		for (Mob m : flyers) {
			spawn.addMob(m);
		}
		
		obj.tick();

		assertEquals(10, spawn.getMobs().size());
		
	}
	
}
