function PokemonImage({ imageName }) {
    const imageUrl = `http://localhost:8080/jip/project/poks/images/${imageName}`;
    return  <div className="pokemon-image-container" style={{ width: '30px', height: '30px', overflow: 'hidden' }}>
    <img src={imageUrl} alt={imageName} className="pokemon-image" />
  </div>
  };
  export default PokemonImage;
  