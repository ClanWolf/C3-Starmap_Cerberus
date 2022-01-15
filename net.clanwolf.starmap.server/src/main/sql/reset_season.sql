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
update _HH_ROUND set round = 1, CurrentRoundStartDate = (select startdate from _HH_SEASON where id = 1) where seasonid = 1;

delete from _HH_ROUTEPOINT; // where season = 1

----- Delete all attacks, attack_characters , attack_vars -----
delete from _HH_ATTACK; // where season = 1
delete from _HH_ATTACK_CHARACTER; // where season = 1
delete from _HH_ATTACKVARS; // where season = 1

----- StarSystemData
update _HH_STARSYSTEMDATA set factionid = factionID_start; // where season = 1

----- Set level for all ships to 1 -----
----- Move all ships to their starting systems (= homeworlds for now) -----

update _HH_JUMPSHIP j set j.level = 1, j.HomeSystemID = (select s.id from _hh_starsystemdata s where s.CapitalWorld = 1 and s.faction_id = j.jumpshipFactionID);
update _HH_JUMPSHIP j set attackready = 1, StarSystemHistory = FORMAT(HomeSystemID, 0));

commit;
