-- Reset a season to starting conditions

-- Do the following steps:
-- * Delete all routepoints                     DONE
-- * Move all ships to their home systems       DONE (set StarSystemHistory to HomeSystemID)
-- * Set level for all ships to 1               DONE
-- * Set ready status to 1 for all ships        DONE
-- * Delete all attacks                         DONE
-- * Delete all attack_characters               DONE
-- * Delete all attack_vars                     DONE
-- * Reset starting date to new value           DONE
-- * Reset round value to 1 in season entry     DONE
-- * Set systems factionid to factionid_start   DONE

-- --- Script -----

-- SERVER STOPPEN !!!

set autocommit=0;
-- --- Reset starting date to new value (in round / Season)
update _HH_ROUND set round = 1, roundphase = 1, CurrentRoundStartDate = ("3060-06-22 12:00:00"), CurrentRoundStartDateRealTime = ("2023-06-22 12:00:06") where season = 1;
-- CurrentRoundStartDate:         Das Startdate der Season ist heute (22.06.2023 --> 3060-06-22 12:00:00, also aktueller Kalendertag, nur 3060 mit 12:00:00)
-- CurrentRoundStartDateRealtime: Das Startdate der Season heute (Realzeit)      --> 2023-06-22 12:00:06 (plus ein paar Sekunden wegen >= )

-- --- Delete all routepoints -----
delete from _HH_ROUTEPOINT where seasonID = 1;

-- --- Delete all attacks, attack_characters, attack_vars -----
delete from _HH_ATTACK where season = 1;                                   -- column is season here, NOT seasonId!
delete from _HH_ATTACK_CHARACTER; -- where season = 1 over attack          -- TODO_C3 check season
delete from _HH_ATTACK_VARS; -- where season = 1 over attack               -- TODO_C3 check season

delete from _HH_ATTACK_STATS where seasonId = 1;                           -- Should stats be kept?
delete from ROLEPLAY_CHARACTER_STATS where seasonId = 1;                   -- Should stats be kept?
delete from STATS_MWO where seasonId = 1;                                  -- Should stats be kept?

delete from DIPLOMACY where seasonId = 1;

-- --- StarSystemData -----
update _HH_STARSYSTEMDATA set factionid = factionID_start,
 LockedUntilRound = null;                                                  -- StarSystemData is there only once,
                                                                           -- no relation to season,
                                                                           -- there is only one season active at a time

-- --- Set level for all ships to 1 -----
-- --- Set all ships to attack ready status -----
-- --- Move all ships to their home systems -----
update _HH_JUMPSHIP j set j.attackready = 1, UnitXP = 0, StarSystemHistory = CAST(HomeSystemID AS CHAR);

-- --- Set the XP for the roleplay characters to 0 (CHECK THIS! We might want to keep the XP for a char between seasons)
update ROLEPLAY_CHARACTER set XP = 0;

update _HH_SEASON set StartDateRealYear = YEAR(CURDATE()) where ID = 1;

commit;

-- SERVER NEU STARTEN !!!
