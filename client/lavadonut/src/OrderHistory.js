import React, { use } from "react";
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
  const [portfolio, setPortfolio] = useState(); // one portfolio

  const { id } = useParams();
  const { name } = useParams();

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
  });

  const userOrders = portfolio.orders;
  const userStocks = portfolio.stocks;

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
        text: `${name} Order History`,
      },
    },
  };

  const orderHistory = {
    labels: userStocks.map(stock => stock.name), 
    datasets: [
      {
        label: "Cost",
        data: userOrders.map(order => order.price), //need to dynamically introduce data
        backgroundColor: [
          "rgba(255, 99, 132, 0.2)",
        ],
        borderColor: [
          "rgba(255, 99, 132, 0.2)",
        ],
        borderWidth: 1,
      },
    ],
  };


  // format page in to display stock and order info
  return (
    <>
      <Bar options={options} data={orderHistory} />
    </>
  );
}

export default OrderHistory;
