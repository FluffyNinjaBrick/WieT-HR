import React from "react";

export const Loading = () => {
  return (
    <div style={{ top: "50%", left: "50%", position: "fixed" }}>
      <span className="fa fa-spinner fa-pulse fa-3x fa-fw text-primary"></span>
    </div>
  );
};

export const LoadingComponent = () => {
  return (
    <div style={{ top: "50%", left: "50%", position: "relative" }}>
      <span className="fa fa-spinner fa-pulse fa-3x fa-fw text-primary"></span>
    </div>
  );
};
