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

set autocommit=0;
-- --- Reset starting date to new value (in round / Season)
UPDATE c3_ROUND set round = 1, CurrentRoundStartDate = (select startdate from c3_SEASON where id = 1) where season = 1;

-- --- Delete all routepoints -----
DELETE FROM c3_ROUTEPOINT where seasonID = 1;

-- --- Delete all attacks, attack_characters , attack_vars -----
DELETE FROM c3_ATTACK where season = 1;                                   -- column is season here, NOT seasonId!
DELETE FROM c3_ATTACK_CHARACTER; // where season = 1 over attack          -- TODO check season
DELETE FROM c3_ATTACKVARS; // where season = 1 over attack                -- TODO check season

DELETE FROM c3_ATTACK_STATS where seasonId = 1;                           -- Should stats be kept?
delete from c3_ROLEPLAY_CHARACTER_STATS where seasonId = 1;                   -- Should stats be kept?
delete from c3_STATS_MWO where seasonId = 1;                                  -- Should stats be kept?

-- --- StarSystemData -----
UPDATE c3_STARSYSTEMDATA set factionid = factionID_start;                 -- StarSystemData is there only once,
                                                                           -- no relation to season,
                                                                           -- there is only one season active at a time

-- --- Set level for all ships to 1 -----
-- --- Set all ships to attack ready status -----
-- --- Move all ships to their home systems -----
UPDATE c3_JUMPSHIP j set j.level = 1, j.attackready = 1, StarSystemHistory = FORMAT(HomeSystemID, 0));

commit;
