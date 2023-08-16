import axios from "axios";
const url = "http://localhost:8080/jip/project/poks"

console.log("PokemonService is being instantiated");
const PokemonService = (function () {
    console.log("I am in Pokemon service")

    const _getPokemonByName = async (name) => {
        try {
            const response = await axios.get(`${url}?name=${name}`);
            if (response && response.data) {
                console.log(response.data)
                return response.data;
            }
            console.log("Error fetching user by username");
        } catch (error) {
            console.error("An error occurred while fetching the user:", error);
        }
        return null; 
    };
    const _listPokemons = async () => {
        try {
            const response = await axios.get(`${url}`);
            if (response && response.data) {
                console.log("listPokemons")
                console.log(response.data)
                return response.data;
            }
            console.log("Error fetching user by username");
        } catch (error) {
            console.error("An error occurred while fetching the user:", error);
        }
        return null; 
    };
    const _deletePokemonByName = async (name) => {
        try {
          const response = await axios.delete(`${url}/name/${name}`);
          return response.data;
        } catch (error) {
          console.error("An error occurred while deleting the Pokémon:", error);
          throw error; 
        }
      };
      const _updatePokemonById = async (id, updatedData) => {
        const response = await axios.put(`${url}/${id}`, updatedData);
        return response.data;
      };
      const _addPokemon = async (formData) => { 
        try {
          const response = await axios.post(`${url}`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data', 
            },
          });
          if (response && response.data) {
            console.log(response.data);
            return response.data;
          }
          console.log("Error adding Pokémon");
        } catch (error) {
          console.error("An error occurred while adding the Pokémon:", error);
        }
        return null;
      };

    const _fetchPoks = async (params) => {
        const response = await axios.get(url, {
        params: {
            name: params.name,
            type: params.type,
            id: params.id,
            
        },
        
    });
    console.log(response.data)
    if(!response) {
        console.log("hata");
        return;
    }
    return response.data;
    };
    const _delete = async() => {
        const response = await axios.delete(
            url, {}
        );
        return response.data;
    };
    console.log("ı amin pokemon service")
    return {

        fetchPoks: _fetchPoks,
        delete: _delete,
        getPokemonByName: _getPokemonByName,
        deletePokemonByName: _deletePokemonByName,
        updatePokemonById: _updatePokemonById,
        addPokemon: _addPokemon,
        listPokemons: _listPokemons,
       
    };



})();

export default PokemonService;