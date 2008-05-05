package net.deuce.clmanager;

import junit.framework.TestCase;
import net.deuce.clmanager.server.DefaultSpamDetector;
import net.deuce.clmanager.server.SpamDetector;

public class TestSpamDetector extends TestCase {

    public void testSpamDetector() {
        String[] validContent = {
            "Well.... I'm the girl who loves the smell of cookouts, driving around at christmas just to see the lights, trying to learn as much as i can and do almost anything I love to learn about ecology and share what I learn with the people around me. I love to do outdoor things, such as RVing, boating, motor cycling, hiking, etc. Honest, loyal, thoughtful I want to be with a man who is connected to his emotions  and is willing to express them. If you think you are this guy please email me include a picture and a lit tle about yourself. I dont have time for games so if your only looking for a booty call move on Please re ply with your picture."
        };
        
        String[] badContent = {
            "<b>Availability <C3><B3></b>In all ways: emotionally, spiritually,",
            "ksdfkjsdkfjsfdJR e a l C h e a p W h o r e s . n e t ksdfj",
            "jsdfkjskdf \"click\" past friends skjdfkljsdkfjsdf",
            "kjdfjkdf bad luck with Cragislist kljdkfj",
            "jsdklfjklsdf kljsdf NASCAR ksjdkfj",
            "<your gdxbwuyimz=\"gdxbwuyimz\" xvbuimrislf=\"xvbuimrislf\" fnlyncxy=\"fnlyncxy\" hiehcfh=\"hiehcfh\" wdmayhy=\"wdmayhy\" lxsgxn=\"lxsgxn\" lk=\"lk\" gmdlrbwb=\"gmdlrbwb\" xocmnjchzdk=\"xocmnjchzdk\" haqy=\"haqy\" oaxtbymwuabn=\"oaxtbymwuabn\" lsqnwejlqvs=\"lsqnwejlqvs\" nbb=\"nbb\" wctkghbgxjk=\"wctkghbgxjk\" kyjmqvxr=\"kyjmqvxr\" sktpnbfymse=\"sktpnbfymse\" aki=\"aki\" qndlk=\"qndlk\">st and ea",
            "<vavra>best",
            "I'm tired of all the fakes and flakes and want to meet a guy that wants lots of good sex. I think after you fuck me you will want more. I'm not too tall, 5'6, 34D and 137 lbs. Write me soon!",
            "NIGHT STAND WITH SOME WOMAN WHO IS EASY, LOOK ELSEWHERE PLEASE. If your interested send me a message and i will go into further detail. I WONT EVEN READ YOUR EMAIL IF YOU DONT HAVE PICTURES",
            "Trying this again so here. I am looking not only for a partner, but also for a good friend. I have a big dog and I love her so you must like animals and if you have a dog, even better. I love all kinds of music and I am looking for someone that is open to all genres. Of course, I'm not looking for that to happen on our first or second date, but I want the possibility to exist, that if we were to hit it off, that a relationship wouldn't be out of the question. IF YOU ARE LOOKING FOR A ONE NIGHT STAND WITH SOME WOMAN WHO IS EASY, LOOK ELSEWHERE PLEASE. If your interested send me a message and i will go into further detail. I WONT EVEN READ YOUR EMAIL IF YOU DONT HAVE PICTURES",
        };
        
        SpamDetector spamDetector = new DefaultSpamDetector();
        
        for (int i=0; i<validContent.length; i++) {
            System.out.println(validContent[i]);
            assertFalse(spamDetector.isSuspicious(validContent[i]));
        }
        for (int i=0; i<badContent.length; i++) {
            System.out.println(badContent[i]);
            assertTrue(spamDetector.isSuspicious(badContent[i]));
        }
    }
}
