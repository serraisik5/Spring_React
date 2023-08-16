import { set } from "lodash";
import React, {useEffect, useState, useCallback, useRef} from "react";
import PokemonService from "../service/PokemonService";
import {Table} from "antd";
import "../designs/Pokemons.css";
import { useLocation } from 'react-router-dom';
import UserService from "../service/UserService";
import PokemonImage from "./PokemonImage";
import "../designs/NewPokemon.css"


//import 'antd/dist/antd.css'; 


  

const PokemonList = ({onUpdatePokemon}) => {
    const [user, setUser] = useState(null);
    const [admin, setAdmin] = useState(null);
    const [newPokemon, setNewPokemon] = useState(null);

    const fileInputRef = useRef(null);
    const location = useLocation();
    const username = location.state ? location.state.username : null; 
    useEffect(() => {
        if (username) {
            UserService.getUserByUsername(username)
                .then(data => {
                    if (data && data.payload && data.payload.length > 0) {
                        const user = data.payload[0];
                        console.log("User in pokemons:", user);
                        setUser(user);
                        setAdmin(user.isAdmin);
                    }
                }).catch(error => {
                    console.log(error);
                });
            }
        }, [username]);

    const columns = [
      {
        title: 'Image',
        dataIndex: 'imageUrl', 
        render: (imageName) => <PokemonImage imageName={imageName} />,
        width: "20%",
      },
        {
          title: 'Id',
          dataIndex: 'id',
          sorter:true,
          width: "10%",
        },
        {
          title: 'Name',
          dataIndex: 'name',
          width: "20%",
        },
        {
            title: 'Type',
            dataIndex: 'type',
            width: "20%",
          },
          ...(admin ? [
            {
              title: 'Delete',
              dataIndex: '',
              key: 'x',
              render: (text, record) => (
                <button onClick={() => deletePokemon(record.name)}>Delete</button>
              ),
            },
            {
              title: 'Update',
              dataIndex: '',
              key: 'x',
              render: (text, record) => (
                <button onClick={() => updatePokemon(record)}>Update</button>
              ),
            }
          ] : [])
        
    
      ];
    const [showAddForm, setShowAddForm] = useState(false);
    const [showUpdateForm, setShowUpdateForm] = useState(false);
    const [currentRecord, setCurrentRecord] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [{isLoading, poks , pagination}, setState] = useState({
        isLoading: true,
        poks: [],
        error: null,
        pagination: {
            current: 1,
            pageSize: 4,
            total: 30
        }
    });
    const deletePokemon = async (name) => {
        try {
          await PokemonService.deletePokemonByName(name); 
          fetchPoks({ pagination });
          if (onUpdatePokemon) {
            onUpdatePokemon();
          }
        } catch (error) {
          console.log(error);
        }
      };
      const updatePokemon = (record) => {
        setCurrentRecord(record); 
        setShowUpdateForm(true); 
        
      };
      const addPokemon = async (e) => {
        e.preventDefault();
      
        const formData = new FormData();
        const fileInput = fileInputRef.current;
        const pokemonData = {
            name: e.target.name.value,
            type: e.target.type.value,
            weight: e.target.weight.value,
            height: e.target.height.value,
            power: e.target.power.value,
            gender: e.target.gender.value,

        };
        const pok = new Blob([JSON.stringify(pokemonData)], {type: "application/json"});
        formData.append("pokemon", pok);

        console.log("pokemon data::::: ",pokemonData)
        if (fileInput && fileInput.files[0]) {
          formData.append("file", fileInput.files[0]);
          console.log("file:::", fileInput.files[0])
          console.log("form data :::::",formData)
        }  
        try {
          const response = await PokemonService.addPokemon(formData);
          setNewPokemon(response.payload); 
          setTimeout(() => setNewPokemon(null), 3000);  
          fetchPoks({ pagination });
          if (onUpdatePokemon) {
            onUpdatePokemon();
          }
          setShowAddForm(false); // Hide the form after adding
        } catch (error) {
          console.log(error);
        }
      };
      
    const searchPoks = useCallback(async () => {
        setState((prevState) => {
            return { ...prevState, isLoading: true };
        });
        try {
            const data = await PokemonService.getPokemonByName(searchTerm);
            console.log("isAdmin in pokemons:", admin)
            setState((prevState) => {
                return { ...prevState, isLoading: false, poks: data?.payload };
            });
        } catch (error) {
            console.log(error);
            setState((prevState) => {
                return { ...prevState, isLoading: false };
            });
        }
    }, [searchTerm]);
    

    const fetchPoks = useCallback(async () => {
        console.log('fetchPoks is being called'); 
        setState( (prevState) => {
            return{
                ...prevState, isLoading:true
            };
        });
        try {
            const data = await PokemonService.fetchPoks({ pagination, name: searchTerm });
            setState( (prevState) => {
                console.log(data)
                return{
                    ...prevState, isLoading:false, poks: data?.payload
                };
                
            });
        } catch (error) {
            console.log(error);
            setState( (prevState) => {
                return{
                    ...prevState, isLoading:false
                };
            });
        }
    },[pagination]);

    useEffect(()=> {
        console.log(pagination);
        fetchPoks({pagination});
  
    },[fetchPoks,pagination]);

    const handleTableChange = (pagination) => {
        setState((prevState) => {
            return {...prevState,pagination};
        });
    }

    
    return (
        <div className="users-container">
          <h2> Pokémon List</h2>
          <div className="search-container">
            <input
              type="text"
              placeholder="Search Pokémon by name"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <div>
            <button onClick={searchPoks}>Search</button>
            <button onClick={() => fetchPoks(pagination)}>Reload</button>
            {admin && <button onClick={() => setShowAddForm(true)}>Add Pokémon</button>}
          </div>
          </div>
          <div className="table-with-forms-container">
           {showUpdateForm && admin &&(
        <div className="add-update-form">
          <h3>Update Pokémon</h3>
          <form
            onSubmit={(e) => {
              e.preventDefault();
              const updatedData = {
                name: e.target.name.value,
                type: e.target.type.value,
                weight: e.target.weight.value,
                height: e.target.height.value,
                power: e.target.power.value,
                gender: e.target.gender.value,
              };
              PokemonService.updatePokemonById(currentRecord.id, updatedData)
                .then(() => {
                  fetchPoks({ pagination });
                  if (onUpdatePokemon) {
                    onUpdatePokemon();
                  }
                  setShowUpdateForm(false);
                })
                .catch((error) => {
                  console.log(error);
                });
            }}
          >
    
             <label className="form-label">
              Name:
              <input type="text" name="name" defaultValue={currentRecord.name} />
            </label>
            <label className="form-label">
              Power:
              <input type="text" name="power" defaultValue={currentRecord.power} />
            </label>
            <label className="form-label">
              Gender:
              <input type="text" name="gender" defaultValue={currentRecord.gender} />
            </label>
            <label className="form-label">
              Height:
              <input type="text" name="height" defaultValue={currentRecord.height} />
            </label>
            <label className="form-label">
              Weight:
              <input type="text" name="weight" defaultValue={currentRecord.weight} />
            </label>
           
            <label className="form-label">
              <div> Type:</div>
              <select name="type" required>
                <option value="water">Water</option>
                <option value="fire">Fire</option>
                <option value="air">Air</option>
                <option value="grass">Grass</option>
              </select>
            </label>
            <div className="form-buttons">
            <button type="submit">Save</button>
            <button type="button" onClick={() => setShowUpdateForm(false)}>Cancel</button>
            </div>
          </form>
        </div>
      )}

      {showAddForm && admin &&(
        <div className="add-update-form">
          <h3>Add Pokémon</h3>
          <form onSubmit={addPokemon}>
      <label className="form-label">
        Name:
        <input type="text" name="name" required />
      </label>
      <label className="form-label">
              Power:
              <input type="text" name="power"  required/>
            </label>
            <label className="form-label">
              Gender:
              <input type="text" name="gender" required />
            </label>
            <label className="form-label">
              Height:
              <input type="text" name="height" required />
            </label>
            <label className="form-label">
              Weight:
              <input type="text" name="weight" required />
            </label>
      <label className="form-label">
        Type:
        <select name="type" required>
          <option value="water">Water</option>
          <option value="fire">Fire</option>
          <option value="air">Air</option>
          <option value="grass">Grass</option>
        </select>
      </label>
      <label className="form-label"> 
        Image:
        <input type="file" ref={fileInputRef} required />
      </label>
      <div className="form-buttons">
      <button type="submit">Add</button>
      <button type="button" onClick={() => setShowAddForm(false)}>Cancel</button>
      </div>
    </form>
        </div>
      )}
      {newPokemon && (
      <div className="newPokemon-animation">
        <h2>New Pokémon!</h2>
        <PokemonImage imageName={newPokemon.imageUrl} />
        <h3>{newPokemon.name}</h3>
      </div>
    )}

      <div className="table-container">
        <Table
          columns={columns}
          rowKey={(record) => record.id}
          dataSource={poks || []}
          loading={isLoading}
          pagination={pagination}
          onChange={handleTableChange}
        />
      </div>
      </div>
      </div>
      );
    };

export default PokemonList;