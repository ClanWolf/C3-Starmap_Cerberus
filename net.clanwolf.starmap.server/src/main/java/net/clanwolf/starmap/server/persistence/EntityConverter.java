/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : https://www.clanwolf.net                           |
 * GitHub      : https://github.com/ClanWolf                        |
 * ---------------------------------------------------------------- |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
 * C3 includes libraries and source code by various authors.        |
 * Copyright (c) 2001-2023, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayStoryPOJO;
import net.clanwolf.starmap.transfer.Dto;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO;
import net.clanwolf.starmap.transfer.util.Compressor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class EntityConverter {
	private static ObjectMapper objectMapper = new ObjectMapper();

	private static String getJsonString(Object o) throws JsonProcessingException {
		return objectMapper.writeValueAsString(o);
	}

	public static <E extends Dto> E convertpojo2dto(Pojo pojo, Class dto) {
		try {
			return (E)objectMapper.readValue(getJsonString(pojo), dto);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <E extends Pojo> E convertdto2pojo(Dto dto, Class pojo) {
		try {
			return (E)objectMapper.readValue(getJsonString(dto), pojo);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static <E extends Dto> E getDto(Pojo pojo) {
		if (pojo == null) return null;

		String namePojo = pojo.getClass().getSimpleName();
		String nameDto = namePojo.replace("POJO", "DTO");
		try {
			return convertpojo2dto(pojo, Class.forName("net.clanwolf.starmap.transfer.dtos." + nameDto));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static <E extends Pojo> E getPojo(Dto dto) {
		if (dto == null) return null;

		String nameDto = dto.getClass().getSimpleName();
		String namePojo = nameDto.replace("DTO", "POJO");
		try {
			return convertdto2pojo(dto, Class.forName("net.clanwolf.starmap.server.persistence.pojos." + namePojo));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static GameState convertGameStateToDTO( GameState state){

		if(state.getObject() instanceof Pojo){
			Object o = getDto((Pojo) state.getObject());
			state.addObject(o);
		}

		if(state.getObject2() instanceof Pojo){
			Object o = getDto((Pojo) state.getObject2());
			state.addObject2(o);
		}

		if(state.getObject3() instanceof Pojo){
			Object o = getDto((Pojo) state.getObject3());
			state.addObject3(o);
		}

		if(state.getObject() instanceof ArrayList){
			ArrayList al = (ArrayList)state.getObject();
			ArrayList hlpAl = new ArrayList();
			Iterator iter = al.iterator();

			while(iter.hasNext()){
				Object o2 = iter.next();
				if(o2 instanceof Pojo) {
					hlpAl.add(getDto((Pojo) o2));
				} else {
					hlpAl.add(o2);
				}
			}
			state.addObject(hlpAl);
		}

		if(state.getObject2() instanceof ArrayList){
			ArrayList al = (ArrayList)state.getObject2();
			ArrayList hlpAl = new ArrayList();
			Iterator iter = al.iterator();

			while(iter.hasNext()){
				Object o2 = iter.next();
				if(o2 instanceof Pojo) {
					hlpAl.add(getDto((Pojo) o2));
				} else {
					hlpAl.add(o2);
				}
			}
			state.addObject2(hlpAl);
		}

		if(state.getObject3() instanceof ArrayList){
			ArrayList al = (ArrayList)state.getObject3();
			ArrayList hlpAl = new ArrayList();
			Iterator iter = al.iterator();

			while(iter.hasNext()){
				Object o2 = iter.next();
				if(o2 instanceof Pojo) {
					hlpAl.add(getDto((Pojo) o2));
				} else {
					hlpAl.add(o2);
				}
			}
			state.addObject3(hlpAl);
		}

		return state;
	}

	public static void convertGameStateToPOJO(GameState state) {

		// https://stackoverflow.com/questions/13370221/persistentobjectexception-detached-entity-passed-to-persist-thrown-by-jpa-and-h

		if (state.getObject() instanceof Dto) {
			Object o = getPojo((Dto) state.getObject());
			state.addObject(o);
		} else if (state.getObject() instanceof byte[]) {
			Object d = Compressor.deCompress((byte[]) state.getObject());
			if (d instanceof Dto) {
				Object o = getPojo((Dto) d);
				state.addObject(o);
			} else if (state.getObject() instanceof ArrayList) {
				ArrayList al = (ArrayList)state.getObject();
				ArrayList hlpAl = new ArrayList();
				Iterator iter = al.iterator();

				while(iter.hasNext()) {
					Object o2 = iter.next();
					if(o2 instanceof Dto) {
						hlpAl.add(getPojo((Dto) o2));
					} else {
						hlpAl.add(o2);
					}
				}
				state.addObject(hlpAl);
			}
		}

		if (state.getObject2() instanceof Dto) {
			Object o = getPojo((Dto) state.getObject2());
			state.addObject2(o);
		} else if (state.getObject2() instanceof byte[]) {
			Object d = Compressor.deCompress((byte[]) state.getObject2());
			if (d instanceof Dto) {
				Object o = getPojo((Dto) d);
				state.addObject2(o);
			} else if (state.getObject2() instanceof ArrayList) {
				ArrayList al = (ArrayList)state.getObject2();
				ArrayList hlpAl = new ArrayList();
				Iterator iter = al.iterator();

				while(iter.hasNext()) {
					Object o2 = iter.next();
					if(o2 instanceof Dto) {
						hlpAl.add(getPojo((Dto) o2));
					} else {
						hlpAl.add(o2);
					}
				}
				state.addObject2(hlpAl);
			}
		}

		if (state.getObject3() instanceof Dto) {
			Object o = getPojo((Dto) state.getObject3());
			state.addObject3(o);
		} else if (state.getObject3() instanceof byte[]) {
			Object d = Compressor.deCompress((byte[]) state.getObject3());
			if (d instanceof Dto) {
				Object o = getPojo((Dto) d);
				state.addObject3(o);
			} else if (state.getObject3() instanceof ArrayList) {
				ArrayList al = (ArrayList)state.getObject3();
				ArrayList hlpAl = new ArrayList();
				Iterator iter = al.iterator();

				while(iter.hasNext()) {
					Object o2 = iter.next();
					if(o2 instanceof Dto) {
						hlpAl.add(getPojo((Dto) o2));
					} else {
						hlpAl.add(o2);
					}
				}
				state.addObject3(hlpAl);
			}
		}

		if (state.getObject() instanceof ArrayList) {
			ArrayList al = (ArrayList)state.getObject();
			ArrayList hlpAl = new ArrayList();
			Iterator iter = al.iterator();

			while(iter.hasNext()){
				Object o2 = iter.next();
				if(o2 instanceof Dto) {
					hlpAl.add(getPojo((Dto) o2));
				} else {
					hlpAl.add(o2);
				}
			}
			state.addObject(hlpAl);
		}

		if (state.getObject2() instanceof ArrayList) {
			ArrayList al = (ArrayList)state.getObject2();
			ArrayList hlpAl = new ArrayList();
			Iterator iter = al.iterator();

			while(iter.hasNext()){
				Object o2 = iter.next();
				if(o2 instanceof Dto) {
					hlpAl.add(getPojo((Dto) o2));
				} else {
					hlpAl.add(o2);
				}
			}
			state.addObject2(hlpAl);
		}

		if (state.getObject3() instanceof ArrayList) {
			ArrayList al = (ArrayList)state.getObject3();
			ArrayList hlpAl = new ArrayList();
			Iterator iter = al.iterator();

			while(iter.hasNext()){
				Object o2 = iter.next();
				if(o2 instanceof Dto) {
					hlpAl.add(getPojo((Dto) o2));
				} else {
					hlpAl.add(o2);
				}
			}
			state.addObject3(hlpAl);
		}
	}

	public static void main(String[] args) {
		RolePlayStoryPOJO test = new RolePlayStoryPOJO();
		test.setStoryName("sdjksdj");

		RolePlayStoryDTO dto = getDto(test);
		RolePlayStoryPOJO pojo = getPojo(dto);

		System.out.println(dto.getStoryName() + " ::: " + dto.getClass().getName());
		System.out.println(pojo.getStoryName() + " ::: " + pojo.getClass().getName());
	}
}
