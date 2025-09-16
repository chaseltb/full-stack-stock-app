import React from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar } from "react-chartjs-2";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

// USE EFFECT TO FETCH PORTFOLIO FROM URL



function OrderHistory() {

    const [userOrders, setOrders] = useState([]); // has many orders
    const [userStock, setStocks] = useState([]); // has many stocks
    const [portfolio, setPortfolio] = useState();  // one portfolio

    const { id } = useParams();

    const url = `http://localhost:8080/api/portfolio/${id}`;

    useEffect(() => {
        fetch(url)
            .then((response) => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected Status Code: ${response.status}`);
                }
            })
            .then((data) => setPortfolio(data))
            .catch(console.log);
    },);

  ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
  );

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: "Chart.js Bar Chart",
      },
    },
  };

  const data = {
    labels: [
      "RENT",
      "GROCERIES",
      "UTILITIES",
      "ENTERTAINMENT",
      "TRANSPORTATION",
    ],
    datasets: [
      {
        label: "Expenses",
        data: [1200, 300, 150, 180, 35],
        backgroundColor: [
          "rgba(255, 99, 132, 0.2)",
          "rgba(255, 99, 132, 0.2)",
          "rgba(255, 99, 132, 0.2)",
          "rgba(255, 99, 132, 0.2)",
          "rgba(255, 99, 132, 0.2)",
        ],
        borderColor: [
          "rgba(255, 99, 132, 0.2)",
          "rgba(255, 99, 132, 0.2)",
          "rgba(255, 99, 132, 0.2)",
          "rgba(255, 99, 132, 0.2)",
          "rgba(255, 99, 132, 0.2)",
          "rgba(255, 99, 132, 0.2)",
        ],
        borderWidth: 1,
      },
    ],
  };

  return (
    <>
      <Bar options={options} data={data} />
    </>
  );
}

export default OrderHistory;
