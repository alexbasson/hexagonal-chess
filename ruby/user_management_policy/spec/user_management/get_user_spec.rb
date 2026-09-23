require 'user_management/user_id'
require 'user_management/user'
require 'user_management/user_repository'
require 'user_management/get_user'

RSpec.describe UserManagement::GetUser do
  subject(:get_user) { described_class.new(user_repository) }

  let(:user_repository) { instance_double(UserManagement::UserRepository) }
  let(:user_id)         { UserManagement::UserId.new(value: 'u1') }
  let(:user)            { UserManagement::User.new(id: user_id, email: 'a@b.com', display_name: 'Alice') }

  it 'returns the user when found' do
    allow(user_repository).to receive(:find_by_id).with(user_id).and_return(user)

    expect(get_user.call(user_id)).to eq(user)
  end

  it 'raises when user not found' do
    allow(user_repository).to receive(:find_by_id).and_return(nil)

    expect { get_user.call(user_id) }.to raise_error(ArgumentError)
  end
end
