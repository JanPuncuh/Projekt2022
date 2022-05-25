import React, {Fragment} from 'react'
import {Marker} from 'react-leaflet';
import MarkerPopup from './MarkerPopup';
import {useState, useEffect} from 'react';
import L from 'leaflet';

const VenueMarkers = (props) => {
    const {venues} = props;
    const [data, setData] = useState([]);
    
    var ip ="";
    venues != null ? ip = "http://146.212.216.121:8080/cesta/session/"+venues
     : ip = "http://146.212.216.121:8080/cesta/session/last";
    

    useEffect(function () {
        const getData = async function () {
        
            const res = await fetch(ip);
            const data = await res.json();
            setData(data);
        }
        getData();
    }, []);

    var VenueLocationIcon = L.icon({
        iconUrl: require('../assets/blue.svg'),
        iconAnchor: null,
        iconSize: [27, 27],
        className: 'leaflet-venue-icon'
    });

    var VenueLocationIcon2 = L.icon({
        iconUrl: require('../assets/red.svg'),
        iconAnchor: null,
        iconSize: [27, 27],
        className: 'leaflet-venue-icon'
    });
    var VenueLocationIcon3 = L.icon({
        iconUrl: require('../assets/orange.svg'),
        iconAnchor: null,
        iconSize: [27, 27],
        className: '2leaflet-venue-icon'
    });




    const markers = data.map((venue, index) => (

        parseFloat(venue.accelerationZ) > 2 || parseFloat(venue.accelerationZ) < -2 ?
            <Marker key={index} position={[venue.latitude, venue.longitude]} icon={VenueLocationIcon2}>
                <MarkerPopup data={venue}/>
            </Marker>
            :
            parseFloat(venue.accelerationZ) > 1.25 || parseFloat(venue.accelerationZ) < -1.25 ?
                <Marker key={index} position={[venue.latitude, venue.longitude]} icon={VenueLocationIcon3}>
                    <MarkerPopup data={venue}/>
                </Marker>
                :
                <Marker key={index} position={[venue.latitude, venue.longitude]} icon={VenueLocationIcon}>
                    <MarkerPopup data={venue}/>
                </Marker>
    ));

    return <Fragment>{markers}</Fragment>
};

export default VenueMarkers;
