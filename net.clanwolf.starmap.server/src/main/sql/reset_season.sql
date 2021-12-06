-- Reset all tables to reset a season to starting conditions.

-- Do the following steps:
-- * Delete all jumppoints
-- * Move all ships to their starting systems (= homeworlds for now)
-- * Set starsystem_history for all ships to the start system id
-- * Set level for all ships to 1
-- * Delete all attacks
-- * Delete all attack_characters
-- * Delete all attack_vars
-- * Delete all events (?)
-- * Reset starting date to new value (in round / Season)
-- * Reset round value to 1 in season entry
-- * Set factionid to factionid_start for all entries in starsystemdata

------ Scripte ------

-- Reset starting date to new value (in round / Season)
update c3._hh_round set round = 1, CurrentRoundStartDate = (select startdate from c3._hh_season where id = 1);

----- Delete all jumppoints -----
delete from _HH_ROUTEPOINT;

----- Delete all attacks, attack_characters , attack_vars -----
delete from _HH_ATTACK;
delete from _HH_ATTACK_CHARACTER;
delete from _HH_ATTACKVARS;

commit;

----- StarSystemData
update c3._hh_starsystemdata set factionid = factionID_start;

----- Set level for all ships to 1 -----
----- Move all ships to their starting systems (= homeworlds for now) -----

update c3._hh_jumpship j set j.level = 1, j.HomeSystemID = (select s.id from c3._hh_starsystemdata s where s.CapitalWorld = 1 and s.faction_id = j.jumpshipFactionID);

update c3._hh_jumpship j set attackready = 1, StarSystemHistory = FORMAT(HomeSystemID, 0));
