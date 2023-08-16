import '../designs/PokemonInfo.css';
import PokemonImage from './PokemonImage'; 

const PokemonInfo = ({ selectedPokemon }) => {
    const genderIcon = selectedPokemon && (selectedPokemon.gender === 'male' ? '♂' : '♀');

    return (
      <div className="pokemon-info">
        {selectedPokemon ? (
          <div>
            <img
              src={`http://localhost:8080/jip/project/poks/images/${selectedPokemon.imageUrl}`}
              alt={selectedPokemon.name}
              className="pokemon-image-info"
            />
            <h3 className="pokemon-name">{selectedPokemon.name}</h3>
            <ul className="pokemon-attributes">
                
            <div>
            <strong>Type</strong>
            <p>{selectedPokemon.type}</p>
          </div>
          <div>
            <strong>Gender</strong>
            <p>{genderIcon} {selectedPokemon.gender}</p>
          </div>
          <div>
            <strong>Power</strong>
            <p>{selectedPokemon.power}</p>
          </div>
          <div>
            <strong>Height</strong>
            <p>{selectedPokemon.height}</p>
          </div>
          <div>
            <strong>Weight</strong>
            <p>{selectedPokemon.weight}</p>
          </div>
          
          
            </ul>
          </div>
        ) : (
          <p>Select a Pokémon to see details</p>
        )}
      </div>
    );
  };
  
  export default PokemonInfo;
  