import React from 'react';
import {Popup} from 'react-leaflet';

const MarkerPopup = (props) => {
  const { name } = props.data;

  return  (<Popup>
    <div style={{
      'width': '200px'
      }}><p style={{
      'white-space': 'pre-wrap'
      }}>User: {props.data.user_id  + "\nLatitude: "  +props.data.latitude + ",\nLongitude: "+  props.data.longitude  + "\nTimestamp: " + props.data.timeStamp }</p></div>
  </Popup>);
};

export default MarkerPopup;
