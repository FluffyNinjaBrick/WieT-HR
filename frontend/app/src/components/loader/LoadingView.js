import React from 'react';

export const Loading = () => {
    return(
        <div style={{top: "50%", left:"50%", position:"fixed"}}>
            <span className="fa fa-spinner fa-pulse fa-3x fa-fw text-primary"></span>
            <p>Loading . . .</p>
        </div>
    );
};