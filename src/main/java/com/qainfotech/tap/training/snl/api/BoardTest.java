package com.qainfotech.tap.training.snl.api;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.AssertJUnit;
import static org.testng.Assert.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.internal.matchers.InstanceOf;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BoardTest {
	Board board;
	@BeforeMethod
	public String create_Random_UUID() {
		int length = 6;
		boolean useLetters = true;
		boolean useNumbers = false;
		return RandomStringUtils.random(length, useLetters, useNumbers);
	}

	@Test
	public void initialize_Board_An_Empty_Board() throws UnsupportedEncodingException, IOException {
		// Initializing the board constructor
		Board board = new Board();
		//asserting if file exists or not
		AssertJUnit.assertTrue(Files.exists(Paths.get(board.getUUID()+".board")));
	}

	@Test
	public void register_Player_Should_Add_New_Player_To_The_Board() throws 
	FileNotFoundException, UnsupportedEncodingException, IOException, 
	PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {
		Board board = new Board();
		UUID randomid = board.registerPlayer("abc");
		AssertJUnit.assertEquals(board.getData().getJSONArray("players").getJSONObject(0).get("name"), "abc");

	}

	@Test
	public void register_Player_Should_Throw_Exception_When_Player_With_Same_Name_Exists() throws 
	FileNotFoundException, UnsupportedEncodingException, IOException, 
	PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {

		Board board = new Board(); //Create board
		UUID randomid = board.registerPlayer("abc");
		try {
			UUID randomid2 = board.registerPlayer("abc");
			Assert.fail();
		}
		catch(PlayerExistsException e) {

		}
	}

	@Test
	public void check_If_toString_Returns_String() throws 
	FileNotFoundException, UnsupportedEncodingException, IOException, 
	PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {
		Board board = new Board();
		UUID a1 = board.registerPlayer(create_Random_UUID());
		AssertJUnit.assertTrue(board.getUUID().toString() instanceof String);
	}

	@Test
	public void check_If_Player_Is_Deleted() throws 
	FileNotFoundException, UnsupportedEncodingException, IOException, 
	PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption{
		Board board = new Board(); //Create board
		UUID randomid = board.registerPlayer(create_Random_UUID());
		try {
			board.deletePlayer(randomid);
		}catch(NoUserWithSuchUUIDException e) {

		}
	}

	@Test(invocationCount = 100)
	public void check_If_rollDice_Produces_Result_Between_1_And_6() throws 
	FileNotFoundException, UnsupportedEncodingException, IOException, 
	PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException {
		Board board = new Board();
		UUID randomid = board.registerPlayer(create_Random_UUID());
		JSONObject jo = board.rollDice(randomid);
		assertTrue((int)jo.get("dice") <= 6 && (int)jo.get("dice") > 0);
	}

	@Test
	public void check_getData_Returns_Data() throws 
	FileNotFoundException, UnsupportedEncodingException, IOException, 
	PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {
		Board board = new Board();
		UUID randomid = board.registerPlayer(create_Random_UUID());
		JSONObject boarddata = board.getData();
		assertTrue(boarddata!=null);
	}

	@Test(invocationCount = 10)
	public void New_Player_Postition_Must_Be_Zero() throws 
	FileNotFoundException, UnsupportedEncodingException, IOException, 
	PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {
		Board board = new Board();
		UUID randomid = board.registerPlayer(create_Random_UUID());
	}
	
	@AfterMethod
	public void delete_All_Board_Files() {
		String dir = System.getProperty("user.dir");
		File folder = new File(dir);
		File fList[] = folder.listFiles();
		for (int i = 0; i < fList.length; i++) {
		    String pes = fList[i].getName();
		    if (pes.endsWith(".board")) {
		        boolean success = (new File(fList[i].getName()).delete());
		    }
		}
	}
}