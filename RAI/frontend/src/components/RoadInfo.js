//izgled posamezne ceste
import React from 'react';

function RoadInfo(props) {

    return (
        <div>
            {props.road.info.map((roadInfo) =>
                <>
                    <hr/>
                    <p>{roadInfo}</p>
                </>
            )}
        </div>
    )
}

export default RoadInfo;