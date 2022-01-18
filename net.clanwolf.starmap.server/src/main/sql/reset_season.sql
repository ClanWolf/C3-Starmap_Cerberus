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

----- Script -----

----- Reset starting date to new value (in round / Season)
update _HH_ROUND set round = 1, CurrentRoundStartDate = (select startdate from _HH_SEASON where id = 1) where seasonid = 1;

----- Delete all routepoints
delete from _HH_ROUTEPOINT; // where season = 1

----- Delete all attacks, attack_characters , attack_vars -----
delete from _HH_ATTACK; // where season = 1
delete from _HH_ATTACK_CHARACTER; // where season = 1
delete from _HH_ATTACKVARS; // where season = 1

----- StarSystemData
update _HH_STARSYSTEMDATA set factionid = factionID_start; // where season = 1

----- Set level for all ships to 1 -----
----- Set all ships to attack ready status -----
----- Move all ships to their home systems -----
update _HH_JUMPSHIP j set j.level = 1, j.attackready = 1, StarSystemHistory = FORMAT(HomeSystemID, 0));

commit;
