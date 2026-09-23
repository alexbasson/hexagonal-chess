require 'user_management/user_id'
require 'user_management/user'
require 'user_management/user_repository'
require 'user_management/create_user'

RSpec.describe UserManagement::CreateUser do
  subject(:create_user) { described_class.new(user_repository) }

  let(:user_repository) { instance_double(UserManagement::UserRepository) }

  before { allow(user_repository).to receive(:save) }

  it 'saves a user with the given email and display name' do
    create_user.call('alice@example.com', 'Alice')

    expect(user_repository).to have_received(:save) do |user|
      expect(user.email).to eq('alice@example.com')
      expect(user.display_name).to eq('Alice')
    end
  end

  it 'returns a user id' do
    result = create_user.call('alice@example.com', 'Alice')

    expect(result).to be_a(UserManagement::UserId)
  end
end
