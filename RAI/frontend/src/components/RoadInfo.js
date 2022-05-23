//izgled posamezne ceste
import React from 'react';

function RoadInfo(props) {

    return (
        <div>
            <p>{props.road.info}</p>
            <hr/>
        </div>
    )
}

export default RoadInfo;