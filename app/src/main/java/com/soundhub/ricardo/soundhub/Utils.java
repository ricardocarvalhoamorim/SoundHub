package com.soundhub.ricardo.soundhub;

import com.google.gson.reflect.TypeToken;
import com.soundhub.ricardo.soundhub.models.GenreItem;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ricardo on 17-03-2015.
 */
public class Utils {

    public static final Type ARRAY_GENRE_ITEMS = new TypeToken<ArrayList<GenreItem>>(){}.getType();
    public static final String GENRE_STATS_ENTRY = "genre_statistics";

    public static final String[] genres = {
            "80s"                  , "Acid Jazz"          , "Acoustic Rock"      , "African"
            , "Alternative"        , "Ambient"            , "Americana"          ,"Arabic"
            ,"Avantgarde"          ,"Bachata"             ,"Bhangra"             ,"Blues"
            ,"Blues Rock"          ,"Bossa Nova"
            ,"Chanson"             ,"Chillout"            ,"Chiptunes"           ,"Choir"
            ,"Classic Rock"        ,"Classical"           ,"Classical Guitar"    ,"Contemporary"
            ,"Country"             ,"Cumbia"              ,"Dance"               ,"Dancehall"
            ,"Death Metal"         ,"Dirty South"         ,"Disco"               ,"Dream Pop"
            ,"Drum & Bass"         ,"Dub"                 ,"Dubstep"             ,"Easy Listening"
            ,"Electro House"       ,"Electronic"          ,"Electronic Pop"      ,"Electronic Rock"
            ,"Folk"                ,"Folk Rock"           ,"Funk"                ,"Glitch"
            ,"Gospel"              ,"Grime"               ,"Grindcore"           ,"Grunge"
            ,"Hard Rock"           ,"Hardcore"            ,"Heavy Metal"         ,"Hip-Hop"
            ,"House"               ,"Indie"               ,"Indie Pop"           ,"Industrial Metal"
            ,"Instrumental Rock"   ,"J-Pop"               ,"Jazz"                ,"Jazz Funk"
            ,"Jazz Fusion"         ,"K-Pop"               ,"Latin"               ,"Latin Jazz"
            ,"Mambo"               ,"Metalcore"           ,"Middle Eastern"      ,"Minimal"
            ,"Modern Jazz"         ,"Moombahton"          ,"New Wave"            ,"Nu Jazz"
            ,"Opera"               ,"Orchestral"          ,"Piano"               ,"Pop"
            ,"Post Hardcore"       ,"Post Rock"           ,"Progressive House"   ,"Progressive Metal"
            ,"Progressive Rock"    ,"Punk"                ,"R&B"                 ,"Rap"
            ,"Reggae"              ,"Reggaeton"           ,"Riddim"              ,"Rock"
            ,"Rock 'n' Roll"       ,"Salsa"               ,"Samba"               ,"Shoegaze"
            ,"Singer / Songwriter" ,"Smooth Jazz"         ,"Soul"                ,"Synth Pop"
            ,"Tech House"          ,"Techno"              ,"Thrash Metal"        ,"Trance"
            ,"Trap"                ,"Trip-hop"            ,"Turntablism" };
}
