import React from "react";

export const Loading = () => {
  return (
    <div
      style={{
        transform: "translate(-25px, -25px)",
        top: "50%",
        left: "50%",
        position: "fixed",
      }}
    >
      <span className="fa fa-spinner fa-pulse fa-3x fa-fw text-primary"></span>
    </div>
  );
};

export const LoadingComponent = () => {
  return (
    <div
      style={{
        transform: "translate(-60px, 0px)",
        top: "50%",
        left: "50%",
        position: "relative",
        margin: "2em",
      }}
    >
      <span className="fa fa-spinner fa-pulse fa-3x fa-fw text-primary"></span>
    </div>
  );
};
