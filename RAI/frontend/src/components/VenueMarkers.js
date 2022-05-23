import React, { Fragment } from 'react'
import {Marker} from 'react-leaflet';
import MarkerPopup from './MarkerPopup';
import { useState, useEffect } from 'react';
import L from 'leaflet';
const VenueMarkers = (props) => {
  const { venues } = props;
  const [data, setData] = useState([]);
  useEffect(function(){
      const getData = async function(){
          const res = await fetch("http://146.212.216.121:8080/cesta");
          const data = await res.json();
          setData(data);
      }
      getData();
  }, []);

   var VenueLocationIcon = L.icon({
     iconUrl: require('../assets/blue.svg'),
    // iconRetinaUrl: require('../assets/venue_location_icon.svg'),
     iconAnchor: null,
     shadowUrl: null,
     shadowSize: null,
     shadowAnchor: null,
     iconSize: [35, 35],
     className: 'leaflet-venue-icon'
   });

   var VenueLocationIcon2 = L.icon({
    iconUrl: require('../assets/red.svg'),
   // iconRetinaUrl: require('../assets/venue_location_icon.svg'),
    iconAnchor: null,
    shadowUrl: null,
    shadowSize: null,
    shadowAnchor: null,
    iconSize: [35, 35],
    className: 'leaflet-venue-icon'
  });
   

  const markers = data.map((venue, index) => (

    parseFloat(venue.accelerationZ ) > 0.4 || parseFloat(venue.accelerationZ ) < -0.4? 
<Marker key={index} position={[ venue.latitude, venue.longitude]} icon={VenueLocationIcon2} >
<MarkerPopup data={venue}/>
</Marker> 
: 
    <Marker key={index} position={[ venue.latitude, venue.longitude]} icon={VenueLocationIcon} >
      <MarkerPopup data={venue}/>
    </Marker>
  ));

  return <Fragment>{markers}</Fragment>
};

export default VenueMarkers;
