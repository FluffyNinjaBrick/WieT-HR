import { ResponsivePie } from "@nivo/pie";

export default function BonusPieChart({ data }) {
  const theme = {
    labels: {
      text: {
        fontSize: "16px",
      },
    },
  };

  return (
    <ResponsivePie
      theme={theme}
      data={data}
      margin={{ top: 40, right: 80, bottom: 80, left: 80 }}
      innerRadius={0.5}
      padAngle={0}
      cornerRadius={0}
      activeOuterRadiusOffset={8}
      borderWidth={1}
      colors={{ scheme: "pink_yellowGreen" }}
      borderColor={{ from: "color", modifiers: [["darker", 0.2]] }}
      arcLinkLabelsSkipAngle={10}
      arcLinkLabelsTextColor="#333333"
      arcLinkLabelsThickness={2}
      arcLinkLabelsColor={{ from: "color" }}
      arcLabelsSkipAngle={10}
      arcLabelsTextColor="white"
    />
  );
}
