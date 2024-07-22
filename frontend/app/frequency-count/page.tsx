'use client'

import { Button, Form, Input } from 'antd'
import axios from 'axios'
import React, { useState } from 'react'

type FieldType = {
    q?: string
    url?: string
}

type FrequencyCountType = {
    keyword: string
    frequency: number
    count: number
    url: string
    searchTime: string
}

const FrequencyCount: React.FC = () => {
    const [result, setResult] = useState<FrequencyCountType>(
        {} as FrequencyCountType
    )

    const onFinish = async (values: FieldType) => {
        try {
            const response = await axios.get('/api/frequency-count', {
                params: values,
            })
            setResult(response.data)
        } catch (error) {
            console.error('Error fetching frequency count:', error)
        }
    }

    const onFinishFailed = (errorInfo: any) => {
        console.error('Failed:', errorInfo)
    }
    return (
        <div className='bg-white rounded-lg p-6'>
            <h1>Search Frequency & Search Count</h1>
            <Form
                name='basic'
                labelCol={{ span: 4 }}
                wrapperCol={{ span: 16 }}
                initialValues={{ remember: true }}
                onFinish={onFinish}
                onFinishFailed={onFinishFailed}
                autoComplete='off'
            >
                <Form.Item<FieldType>
                    label='Keyword'
                    name='q'
                    rules={[
                        {
                            required: true,
                            message: 'Please input your keyword!',
                        },
                    ]}
                    className='mb-4'
                >
                    <Input />
                </Form.Item>

                <Form.Item<FieldType>
                    label='Url'
                    name='url'
                    rules={[
                        { required: true, message: 'Please input your URL!' },
                    ]}
                    className='mb-4'
                >
                    <Input />
                </Form.Item>

                <Form.Item className='text-center'>
                    <Button type='primary' htmlType='submit'>
                        Submit
                    </Button>
                </Form.Item>
            </Form>
            <div className='mt-6 w-full max-w-md bg-white p-6 rounded-lg shadow-md'>
                {Object.keys(result).length !== 0 ? (
                    <div>
                        <p>
                            There are a total of {result.frequency} occurrences
                            of the keyword on the page.
                        </p>
                        <p>This word has been searched {result.count} times.</p>
                    </div>
                ) : (
                    <p>
                        Please input the keyword and URL to get the frequency
                        count.
                    </p>
                )}
            </div>
        </div>
    )
}

export default FrequencyCount
