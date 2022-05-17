import React, { Fragment } from 'react'
import {Marker} from 'react-leaflet';
import {VenueLocationIcon} from './VenueLocationIcon';
import MarkerPopup from './MarkerPopup';
import { useState, useEffect } from 'react';

const VenueMarkers = (props) => {
  const { venues } = props;
  const [data, setData] = useState([]);
  useEffect(function(){
      const getData = async function(){
          const res = await fetch("http://46.164.22.42:8080/cesta");
          const data = await res.json();
          setData(data);
      }
      getData();
  }, []);

  const markers = data.map((venue, index) => (
    

    <Marker key={index} position={[ venue.latitude, venue.longitude]} icon={VenueLocationIcon} >
      <MarkerPopup data={venue}/>
    </Marker>
  ));

  return <Fragment>{markers}</Fragment>
};

export default VenueMarkers;
