'use client'
import React, { useEffect, useState } from 'react'
import { ValidationType } from '../types/datavalidation'
import { Table } from 'antd'
import { title } from 'process'
import axios from 'axios'
import { render } from 'react-dom'

function DataValidation() {
    const [data, setData] = useState<ValidationType[]>([])

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('/api/data-validation')
                setData(response.data)
            } catch (error) {
                console.error('Fetch error:', error)
            }
        }
        fetchData()
    }, [])

    const columns = [
        {
            title: 'Product Name',
            dataIndex: 'productName',
        },
        {
            title: 'Price',
            dataIndex: 'price',
        },
        {
            title: 'Image URL',
            dataIndex: 'imageUrl',
        },
        {
            title: 'Product URL',
            dataIndex: 'productUrl',
        },
        {
            title: 'Product Description',
            dataIndex: 'productDescription',
            ellipsis: true,
        },
        {
            title: 'Valid Name',
            dataIndex: 'validName',
            render: (isValidName: boolean) => (isValidName ? 'Yes' : 'No'),
            filters: [
                { text: 'Yes', value: true },
                { text: 'No', value: false },
            ],
            onFilter: (value, record) => record.validName === value,
        },
        {
            title: 'Valid Price',
            dataIndex: 'validPrice',
            render: (isValidPrice: boolean) => (isValidPrice ? 'Yes' : 'No'),
            filters: [
                { text: 'Yes', value: true },
                { text: 'No', value: false },
            ],
            onFilter: (value, record) => record.validPrice === value,
        },
        {
            title: 'Valid Image URL',
            dataIndex: 'validImageUrl',
            render: (isValidImageUrl: boolean) =>
                isValidImageUrl ? 'Yes' : 'No',
            filters: [
                { text: 'Yes', value: true },
                { text: 'No', value: false },
            ],
            onFilter: (value, record) => record.validImageUrl === value,
        },
        {
            title: 'Valid Product URL',
            dataIndex: 'validProductUrl',
            render: (isValidProductUrl: boolean) =>
                isValidProductUrl ? 'Yes' : 'No',
            filters: [
                { text: 'Yes', value: true },
                { text: 'No', value: false },
            ],
            onFilter: (value, record) => record.validProductUrl === value,
        },
        {
            title: 'Valid Description',
            dataIndex: 'validDescription',
            render: (isValidDescription: boolean) =>
                isValidDescription ? 'Yes' : 'No',
            filters: [
                { text: 'Yes', value: true },
                { text: 'No', value: false },
            ],
            onFilter: (value, record) => record.validDescription === value,
        },
    ]
    return <Table dataSource={data} columns={columns} />
}

export default DataValidation
